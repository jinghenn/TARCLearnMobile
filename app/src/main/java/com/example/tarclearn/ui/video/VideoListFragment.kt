package com.example.tarclearn.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.adapter.VideoRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentVideoListBinding
import com.example.tarclearn.factory.ChapterViewModelFactory
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.viewmodel.VideoListViewModel

class VideoListFragment : Fragment() {
    private lateinit var binding: FragmentVideoListBinding
    private lateinit var viewModel: VideoListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoListBinding.inflate(inflater, container, false)
        val vmFactory = ChapterViewModelFactory(ChapterRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(VideoListViewModel::class.java)

        return binding.root
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

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchVideoList(requireActivity().intent.getIntExtra("chapterId",0))
    }


}