package com.example.tarclearn.ui.fragment

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
import com.example.tarclearn.databinding.FragmentAddCourseBinding
import com.example.tarclearn.factory.AddCourseViewModelFactory
import com.example.tarclearn.repository.Repository
import com.example.tarclearn.viewmodel.AddCourseViewModel

class AddCourseFragment : Fragment() {
    private lateinit var binding: FragmentAddCourseBinding
    private lateinit var viewModel: AddCourseViewModel
    private lateinit var sharedPref: SharedPreferences
    private val args: AddCourseFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddCourseBinding.inflate(inflater, container, false)
        //create viewmodel
        val vmFactory = AddCourseViewModelFactory(Repository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(AddCourseViewModel::class.java)
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (args.mode) {
            1 -> binding.btnCreateCourse.text = getString(R.string.label_create)
            2 -> {
                binding.btnCreateCourse.text = getString(R.string.label_save)
                binding.tvCourseId.isEnabled = false
                binding.tvCourseId.setText(args.courseId)
            }
        }
        binding.tvCourseId.doAfterTextChanged {
            if (it.toString() == "") {
                binding.tvCourseIdLayout.isErrorEnabled = true
                binding.tvCourseIdLayout.error = "Course ID cannot be empty"
            } else {
                binding.tvCourseIdLayout.isErrorEnabled = false
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
        binding.btnCancelCreateCourse.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnCreateCourse.setOnClickListener {
            val courseId = binding.tvCourseId.text.toString()
            val courseName = binding.tvCourseName.text.toString()
            val desc = binding.tvCourseDescription.text.toString()
            if (courseId == "") {
                binding.tvCourseIdLayout.isErrorEnabled = true
                binding.tvCourseIdLayout.error = "Course ID cannot be empty"
            }
            if (courseName == "") {
                binding.tvCourseNameLayout.isErrorEnabled = true
                binding.tvCourseNameLayout.error = "Course Name cannot be empty"
            }
            if (courseId != "" && courseName != "") {
                when (args.mode) {
                    1 -> {
                        viewModel.createCourse(
                            sharedPref.getString(getString(R.string.key_user_id), "") ?: "",
                            courseId,
                            courseName,
                            desc
                        )
                    }
                    2 -> {
                        viewModel.editCourse(
                            courseId,
                            courseName,
                            desc
                        )
                    }
                }
            }
        }

        viewModel.success.observe(viewLifecycleOwner) {
            val text = when(args.mode){
                1 -> "Course: ${viewModel.course.value?.courseId} created successfully"
                2 -> "Course: ${viewModel.course.value?.courseId} updated successfully"
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
                binding.tvCourseIdLayout.isErrorEnabled = true
                binding.tvCourseIdLayout.error = it
            }
        }
    }
}