package com.example.tarclearn.ui.course

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.R
import com.example.tarclearn.databinding.FragmentManageCourseBinding
import com.example.tarclearn.factory.ManageCourseViewModelFactory
import com.example.tarclearn.repository.CourseRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.course.ManageCourseViewModel

class ManageCourseFragment : Fragment() {
    private lateinit var binding: FragmentManageCourseBinding
    private lateinit var viewModel: ManageCourseViewModel
    private lateinit var sharedPref: SharedPreferences
    private val args: ManageCourseFragmentArgs by navArgs()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageCourseBinding.inflate(inflater, container, false)
        //create viewmodel
        val vmFactory = ManageCourseViewModelFactory(CourseRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(ManageCourseViewModel::class.java)
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        userId = sharedPref.getString(getString(R.string.key_user_id), "")!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextFieldWithCourseDetail()
        setupButtonWithMode()

        binding.tvCourseCode.doAfterTextChanged {
            if (it.toString() == "") {
                binding.tvCourseCodeLayout.isErrorEnabled = true
                binding.tvCourseCodeLayout.error = "Course ID cannot be empty"
            } else {
                binding.tvCourseCodeLayout.isErrorEnabled = false
            }
        }
        binding.tvCourseName.doAfterTextChanged {
            if (it.toString() == "") {
                binding.tvCourseNameLayout.isErrorEnabled = true
                binding.tvCourseNameLayout.error = "Course Name cannot be empty"
            } else {
                binding.tvCourseNameLayout.isErrorEnabled = false
            }
        }
        viewModel.success.observe(viewLifecycleOwner) {
            val text = when (args.mode) {
                Constants.MODE_CREATE -> "Course: ${viewModel.course.value?.courseCode} created successfully"
                Constants.MODE_EDIT -> "Course: ${viewModel.course.value?.courseCode} updated successfully"
                else -> "Success"
            }
            if (it == true) {
                Toast.makeText(
                    requireContext(),
                    text,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetSuccessFlag()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvCourseCodeLayout.isErrorEnabled = true
                binding.tvCourseCodeLayout.error = it
            }
        }
    }

    private fun setupTextFieldWithCourseDetail() {
        if (args.mode == Constants.MODE_EDIT) {
            viewModel.fetchCourse(args.courseId)
            viewModel.course.observe(viewLifecycleOwner) {
                it?.let {
                    binding.tvCourseCode.setText(it.courseCode)
                    binding.tvCourseName.setText(it.courseTitle)
                    binding.tvCourseDescription.setText(it.courseDescription)
                }
            }
        }

    }

    private fun setupButtonWithMode() {
        val btnOk = binding.btnOkCourse
        when (args.mode) {
            Constants.MODE_CREATE -> {
                btnOk.setText(getString(R.string.label_create))
                btnOk.setOnClickListener {
                    val courseId = getCourseCode()
                    val courseName = getCourseName()
                    if (courseId != "" && courseName != "") {
                        viewModel.createCourse(
                            userId,
                            courseId,
                            courseName,
                            binding.tvCourseDescription.text.toString()
                        )
                    }
                }
            }
            Constants.MODE_EDIT -> {
                btnOk.setText(getString(R.string.label_save))
                btnOk.setOnClickListener {
                    val courseCode = getCourseCode()
                    val courseName = getCourseName()
                    if (courseCode != "" && courseName != "") {
                        viewModel.editCourse(
                            args.courseId,
                            courseCode,
                            courseName,
                            binding.tvCourseDescription.text.toString()
                        )
                    }
                }
            }
        }
        binding.btnCancelCourse.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getCourseCode(): String {
        val courseCode = binding.tvCourseCode.text.toString()
        if (courseCode == "") {
            binding.tvCourseCodeLayout.isErrorEnabled = true
            binding.tvCourseCodeLayout.error = "Course ID cannot be empty"
            return ""
        }
        return courseCode
    }

    private fun getCourseName(): String {
        val courseName = binding.tvCourseName.text.toString()
        if (courseName == "") {
            binding.tvCourseNameLayout.isErrorEnabled = true
            binding.tvCourseNameLayout.error = "Course Name cannot be empty"
            return ""
        }
        return courseName
    }
}
