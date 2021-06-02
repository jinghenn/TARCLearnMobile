package com.example.tarclearn.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tarclearn.R
import com.example.tarclearn.adapter.VideoRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentVideoListBinding
import com.example.tarclearn.factory.ChapterViewModelFactory
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.VideoListViewModel
import java.util.*
import kotlin.properties.Delegates

class VideoListFragment : Fragment() {
    private lateinit var binding: FragmentVideoListBinding
    private lateinit var viewModel: VideoListViewModel
    private var chapterId by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoListBinding.inflate(inflater, container, false)
        val vmFactory = ChapterViewModelFactory(ChapterRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(VideoListViewModel::class.java)
        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initializeMenu()
        viewModel.fetchVideoList(chapterId, binding.menuMode.text.toString().toUpperCase(Locale.ROOT))

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoRecyclerView = binding.videoRecyclerView

        viewModel.chapterList.observe(viewLifecycleOwner) {
            it?.let {
                val adapter = VideoRecyclerViewAdapter()
                adapter.videoList = it as MutableList
                videoRecyclerView.adapter = adapter
            }
        }
        binding.fabAddVideo.setOnClickListener {
            val action = VideoListFragmentDirections.actionVideoListFragmentToManageVideoFragment(
                Constants.MODE_CREATE,
                -1
            )
            findNavController().navigate(action)
        }
    }


    private fun initializeMenu() {
        val items = listOf("Lecture", "Tutorial", "Practical")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, items)
        (binding.menuMode as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.menuMode.setText(binding.menuMode.adapter.getItem(0).toString(), false)

        binding.menuMode.doAfterTextChanged {
            viewModel.fetchVideoList(chapterId, binding.menuMode.text.toString().toUpperCase(Locale.ROOT))
        }
    }


}