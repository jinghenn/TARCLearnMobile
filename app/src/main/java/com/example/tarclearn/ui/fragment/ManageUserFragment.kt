package com.example.tarclearn.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.adapter.CourseUserRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentManageUserBinding
import com.example.tarclearn.factory.ManageUserViewModelFactory
import com.example.tarclearn.model.UserDto
import com.example.tarclearn.repository.Repository
import com.example.tarclearn.viewmodel.ManageUserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.coroutineScope

class ManageUserFragment : Fragment() {
    private lateinit var binding: FragmentManageUserBinding
    private lateinit var viewModel: ManageUserViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private var courseId = ""
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
        val repository = Repository()
        val viewModelFactory = ManageUserViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ManageUserViewModel::class.java)

        viewModel.fetchCourseList(currentUserId)

        //initialize dropdown menu
        viewModel.courseList.observe(viewLifecycleOwner,{
            val items = viewModel.getCourseListWithName()
            val adapter = ArrayAdapter(requireContext(), R.layout.manage_user_menu_layout, items)
            (binding.etCourse as? AutoCompleteTextView)?.setAdapter(adapter)
        })
        binding.etCourse.doAfterTextChanged {
            courseId = it.toString().substringBefore(" ")
            viewModel.fetchUserList(courseId)
            binding.courseMenu.isErrorEnabled = false
            adapter = CourseUserRecyclerViewAdapter(courseId, currentUserId)
        }
        //setup recyclerview
        recyclerView = binding.courseWithUserRecyclerView

        viewModel.userList.observe(viewLifecycleOwner){
            it?.let{
                adapter.userList = it as MutableList<UserDto>
            }
            recyclerView.adapter = adapter
        }

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 120
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            it?.let{
                binding.etLayoutAddUser.isErrorEnabled = true
                binding.etLayoutAddUser.error = it
            }
        }
        viewModel.successMessage.observe(viewLifecycleOwner){
            it?.let{
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddUser.setOnClickListener{
            val userId = binding.etAddUser.text.toString()
            if(courseId == ""){
                binding.courseMenu.isErrorEnabled = true
                binding.courseMenu.error = "Please select a course"
            }else if(userId == ""){
                binding.etLayoutAddUser.isErrorEnabled = true
                binding.etLayoutAddUser.error = "User ID cannot be empty"
            }else{
                viewModel.enrol(courseId, userId)
            }

        }
        binding.etAddUser.doAfterTextChanged{
            binding.etLayoutAddUser.isErrorEnabled = false
        }
    }
}