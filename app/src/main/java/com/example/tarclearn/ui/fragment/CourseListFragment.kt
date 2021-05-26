package com.example.tarclearn.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tarclearn.R
import com.example.tarclearn.adapter.CourseRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentCourseListBinding
import com.example.tarclearn.factory.CourseViewModelFactory
import com.example.tarclearn.repository.Repository
import com.example.tarclearn.viewmodel.CourseViewModel

class CourseListFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentCourseListBinding
    private lateinit var viewModel: CourseViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCourseListBinding.inflate(inflater, container, false)
        val repository = Repository()
        val viewModelFactory = CourseViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity().viewModelStore, viewModelFactory).get(CourseViewModel::class.java)

        //fetch the course enrolled by the user
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        val userId = sharedPref.getString(getString(R.string.key_user_id), "")
        if (userId != null) {
            viewModel.fetchCourseList(userId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setup the recycler view of course list
        val adapter = CourseRecyclerViewAdapter()
        viewModel.courseList.observe(viewLifecycleOwner, {
            it?.let {
                if (it.size == 0) {
                    binding.tvEmpty.text = getString(R.string.label_nothing_to_show)
                } else {
                    binding.tvEmpty.text = ""
                    adapter.courseList = it
                }
            }
        })
        binding.courseRecyclerView.adapter = adapter

        binding.fabAddCourse.setOnClickListener {
            val action = CourseListFragmentDirections.actionCourseFragmentToAddCourseFragment(1, "")
            findNavController().navigate(action)
        }
    }


}