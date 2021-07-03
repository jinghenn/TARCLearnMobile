package com.example.tarclearn.ui.discussion

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.R
import com.example.tarclearn.adapter.student.MyDiscussionsRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentMyDiscussionsBinding
import com.example.tarclearn.factory.UserViewModelFactory
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.repository.UserRepository
import com.example.tarclearn.viewmodel.discussion.MyDiscussionsViewModel

class MyDiscussionsFragment : Fragment() {
    private lateinit var binding: FragmentMyDiscussionsBinding
    private lateinit var viewModel: MyDiscussionsViewModel
    private lateinit var userId: String
    private lateinit var sharedPref: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyDiscussionsBinding.inflate(inflater, container, false)
        val vmFactory = UserViewModelFactory(UserRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(MyDiscussionsViewModel::class.java)

        sharedPref = requireContext().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        userId = sharedPref.getString(getString(R.string.key_user_id), null) ?: ""
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (userId != "") {
            viewModel.fetchThreadList(userId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.myDiscussionRecyclerView
        val adapter = MyDiscussionsRecyclerViewAdapter()
        recyclerView.adapter = adapter
        viewModel.threadList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.threadList = it as MutableList<DiscussionThreadDto>
            }
        }
    }

}