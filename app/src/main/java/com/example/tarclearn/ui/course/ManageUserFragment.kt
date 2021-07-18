package com.example.tarclearn.ui.course

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.R
import com.example.tarclearn.adapter.CourseUserRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentManageUserBinding
import com.example.tarclearn.factory.ManageUserViewModelFactory
import com.example.tarclearn.model.UserDto
import com.example.tarclearn.repository.CourseRepository
import com.example.tarclearn.repository.UserRepository
import com.example.tarclearn.viewmodel.course.ManageUserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ManageUserFragment : Fragment() {
    private lateinit var binding: FragmentManageUserBinding
    private lateinit var viewModel: ManageUserViewModel
    private lateinit var sharedPref: SharedPreferences

    private var courseId = -1
    private var currentUserId = ""
    private var currentEmail = ""

    private var adapter = CourseUserRecyclerViewAdapter(courseId, currentUserId)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageUserBinding.inflate(inflater, container, false)

        //get user id
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        currentUserId = sharedPref.getString(getString(R.string.key_user_id), "")!!
        currentEmail = sharedPref.getString(getString(R.string.key_email), null) ?: ""
        //create viewmodel
        val viewModelFactory = ManageUserViewModelFactory(UserRepository(), CourseRepository())
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ManageUserViewModel::class.java)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCourseList(currentUserId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDropDownList()
        setupFabAddUser()
        setupFabRemoveUser()
        //setup recyclerview
        val recyclerView = binding.courseWithUserRecyclerView
        binding.etCourse.doAfterTextChanged {
            binding.courseMenu.isErrorEnabled = false
        }
        viewModel.userList.observe(viewLifecycleOwner) {
            val adapter = CourseUserRecyclerViewAdapter(courseId, currentUserId)
            it?.let {
                adapter.userList = it as MutableList<UserDto>
            }
            recyclerView.adapter = adapter
        }
        viewModel.emailList.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.fetchUserList(courseId)
            }
        }
        viewModel.failedEmailList.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.fetchUserList(courseId)
                val iterator = it.iterator()
                var emailList = ""
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    emailList += if (iterator.hasNext()) {
                        "\t$next,\n"
                    } else {
                        "\t$next"
                    }
                }
                val scrollView = View.inflate(requireContext(), R.layout.add_user_fail, null)
                val failedEt: TextView = scrollView.findViewById(R.id.tv_fail_email_list)
                failedEt.movementMethod = ScrollingMovementMethod()
                failedEt.text = emailList
                MaterialAlertDialogBuilder(requireContext())
                    .setView(scrollView)
                    .setMessage("The operation failed on the following email:")
                    .setPositiveButton("Ok", null)
                    .show()
            }

        }

        viewModel.successMessage.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setupDropDownList() {
        //initialize dropdown menu
        viewModel.courseList.observe(viewLifecycleOwner, { list ->
            list?.let {
                val courses = mutableListOf<String>()
                val ids = mutableListOf<Int>()
                it.forEach { course ->
                    courses.add("${course.courseCode} ${course.courseTitle}")
                    ids.add(course.courseId)
                }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, courses)
                val ddlCourse = binding.etCourse as? AutoCompleteTextView
                ddlCourse?.setAdapter(adapter)
                ddlCourse?.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        viewModel.fetchUserList(ids[position])
                        courseId = ids[position]

                    }
            }

        })
    }

    private fun setupFabAddUser() {
        val fab = binding.fabAddUser

        fab.setOnClickListener {
            val editTextLayout = View.inflate(requireContext(), R.layout.add_user_edit_text, null)
            val editTextInputLayout: TextInputLayout = editTextLayout
                .findViewById(R.id.et_email_list_layout)
            val editText: TextInputEditText = editTextLayout.findViewById(R.id.et_email_list)
            editText.doAfterTextChanged {
                if (it.toString() != "") {
                    editTextInputLayout.isErrorEnabled = false
                }
            }
            if (courseId == -1) {
                binding.courseMenu.isErrorEnabled = true
                binding.courseMenu.error = "Course cannot be empty"
            } else {
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Add User(s)")
                    .setMessage("Enter emails of users separated by comma")
                    .setView(editTextLayout)
                    .setPositiveButton(getString(R.string.label_add), null)
                    .setNegativeButton(getString(R.string.label_cancel), null)
                    .create()

                dialog.setOnShowListener {
                    val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btn.setOnClickListener {
                        if (editText.text.toString() == "") {

                            editTextInputLayout.isErrorEnabled = true
                            editTextInputLayout.error = "Email(s) cannot be empty"
                        } else {
                            val emailList = viewModel.validateEmailList(editText.text.toString())

                            if (emailList.isNotEmpty()) {
                                viewModel.enrol(courseId, emailList)
                                dialog.dismiss()
                            } else {
                                editTextInputLayout.isErrorEnabled = true
                                editTextInputLayout.error = "Invalid email"
                            }

                        }
                    }
                }
                dialog.show()
            }
        }
    }

    private fun setupFabRemoveUser() {
        val fab = binding.fabRemoveUser
        fab.setOnClickListener {
            val editTextLayout = View.inflate(requireContext(), R.layout.add_user_edit_text, null)
            val editTextInputLayout: TextInputLayout = editTextLayout
                .findViewById(R.id.et_email_list_layout)
            val editText: TextInputEditText = editTextLayout.findViewById(R.id.et_email_list)
            editText.doAfterTextChanged {
                if (it.toString() != "") {
                    editTextInputLayout.isErrorEnabled = false
                }
            }
            if (courseId == -1) {
                binding.courseMenu.isErrorEnabled = true
                binding.courseMenu.error = "Course cannot be empty"
            } else {
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Remove User(s)")
                    .setMessage("Enter emails of users separated by comma")
                    .setView(editTextLayout)
                    .setPositiveButton(getString(R.string.label_remove), null)
                    .setNegativeButton(getString(R.string.label_cancel), null)
                    .create()

                dialog.setOnShowListener {
                    val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btn.setOnClickListener {
                        if (editText.text.toString() == "") {

                            editTextInputLayout.isErrorEnabled = true
                            editTextInputLayout.error = "Email(s) cannot be empty"
                        } else {
                            val emailList = viewModel.validateEmailList(editText.text.toString())

                            if (emailList.isNotEmpty()) {
                                viewModel.unenrol(courseId, emailList, currentEmail)
                                dialog.dismiss()
                            } else {
                                editTextInputLayout.isErrorEnabled = true
                                editTextInputLayout.error = "Invalid email"
                            }

                        }
                    }
                }
                dialog.show()
            }

        }
    }
}