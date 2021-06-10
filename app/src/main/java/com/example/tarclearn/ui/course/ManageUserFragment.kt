package com.example.tarclearn.ui.course

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
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
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ManageUserFragment : Fragment() {
    private lateinit var binding: FragmentManageUserBinding
    private lateinit var viewModel: ManageUserViewModel
    private lateinit var sharedPref: SharedPreferences

    private var courseId = -1
    private var currentUserId = ""

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
        setupBottomSheet()

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

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                binding.etLayoutAddUser.isErrorEnabled = true
                binding.etLayoutAddUser.error = it
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

    private fun setupBottomSheet() {
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 120
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.btnAddUser.setOnClickListener {
            val userId = binding.etAddUser.text.toString()
            if (courseId == -1) {
                binding.courseMenu.isErrorEnabled = true
                binding.courseMenu.error = "Please select a course"
            } else if (userId == "") {
                binding.etLayoutAddUser.isErrorEnabled = true
                binding.etLayoutAddUser.error = "User ID cannot be empty"
            } else {
                viewModel.enrol(courseId, userId)
            }

        }
        binding.etAddUser.doAfterTextChanged {
            binding.etLayoutAddUser.isErrorEnabled = false
        }
    }
}