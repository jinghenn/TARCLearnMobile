package com.example.tarclearn.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tarclearn.R
import com.example.tarclearn.databinding.FragmentCourseBinding

class CourseFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean(getString(R.string.key_is_logged_in), false)
        if(!isLoggedIn){
            findNavController().navigate(R.id.action_courseFragment_to_loginFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCourseBinding.inflate(inflater, container, false)

        return binding.root
    }

}