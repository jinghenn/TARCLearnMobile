package com.example.tarclearn.ui.discussion

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
import com.example.tarclearn.adapter.DiscussionRecyclerViewAdapter
import com.example.tarclearn.adapter.student.StudentDiscussionRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentDiscussionListBinding
import com.example.tarclearn.factory.ChapterViewModelFactory
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.discussion.DiscussionListViewModel
import kotlin.properties.Delegates

class DiscussionListFragment : Fragment() {
    private lateinit var viewModel: DiscussionListViewModel
    private lateinit var binding: FragmentDiscussionListBinding
    private var chapterId by Delegates.notNull<Int>()
    private lateinit var sharedPref: SharedPreferences
    private var isLect = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscussionListBinding.inflate(inflater, container, false)
        val vmFactory = ChapterViewModelFactory(ChapterRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(DiscussionListViewModel::class.java)
        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)

        sharedPref = requireContext()
            .getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)
        isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (chapterId != -1) {
            viewModel.fetchThreadList(chapterId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.threadList.observe(viewLifecycleOwner) {
            it?.let {
                if (isLect) {
                    val adapter = DiscussionRecyclerViewAdapter()
                    adapter.threadList = it as MutableList
                    binding.discussionRecyclerView.adapter = adapter
                } else {
                    val adapter = StudentDiscussionRecyclerViewAdapter()
                    adapter.threadList = it as MutableList
                    binding.discussionRecyclerView.adapter = adapter
                }

            }
        }
        binding.fabAddDiscussion.setOnClickListener {
            val action = DiscussionListFragmentDirections
                .actionDiscussionListFragmentToManageDiscussionFragment(Constants.MODE_CREATE, -1)
            findNavController().navigate(action)
        }
    }
}