package com.example.tarclearn.ui.course

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tarclearn.R
import com.example.tarclearn.adapter.CourseRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentCourseListBinding
import com.example.tarclearn.factory.UserViewModelFactory
import com.example.tarclearn.repository.UserRepository
import com.example.tarclearn.viewmodel.course.CourseListViewModel

class CourseListFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentCourseListBinding
    private lateinit var viewModel: CourseListViewModel
    private var isLect = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCourseListBinding.inflate(inflater, container, false)
        val repository = UserRepository()
        val viewModelFactory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity().viewModelStore, viewModelFactory).get(
            CourseListViewModel::class.java
        )

        //fetch the course enrolled by the user
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        val userId = sharedPref.getString(getString(R.string.key_user_id), "")
        if (userId != null) {
            viewModel.fetchCourseList(userId)
        }
        isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLect) {
            binding.rootLayout.removeView(binding.coordLayout)
            val params = binding.courseRecyclerView.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            binding.courseRecyclerView.layoutParams = params
        }
        //setup the recycler view of course list
        val adapter = CourseRecyclerViewAdapter()
        viewModel.courseList.observe(viewLifecycleOwner, {
            it?.let {
                if (it.isEmpty()) {
                    binding.tvEmpty.text = getString(R.string.label_nothing_to_show)
                } else {
                    binding.tvEmpty.text = ""
                    adapter.courseList = it
                }
            }
        })
        binding.courseRecyclerView.adapter = adapter

        binding.fabAddCourse.setOnClickListener {
            val action =
                CourseListFragmentDirections.actionCourseFragmentToManageCourseFragment(
                    1,
                    -1
                )
            findNavController().navigate(action)
        }
    }


}