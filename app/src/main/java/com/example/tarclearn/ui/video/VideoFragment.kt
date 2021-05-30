package com.example.tarclearn.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.databinding.FragmentVideoBinding
import com.example.tarclearn.factory.MaterialViewModelFactory
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.viewmodel.VideoViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class VideoFragment : Fragment() {
    private lateinit var binding: FragmentVideoBinding
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var viewModel: VideoViewModel

    private val args: VideoFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        val vmFactory = MaterialViewModelFactory(MaterialRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(VideoViewModel::class.java)
        viewModel.fetchVideoDetail(args.materialId)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.video.observe(viewLifecycleOwner){
            it?.let{
                binding.tvVideoTitle.text = it.materialTitle
                binding.tvVideoDescription.text = it.materialDescription
            }
        }
        binding.playerView.player = exoPlayer
        val item: MediaItem = MediaItem.Builder()
            .setUri("http://192.168.0.72:50000/api/videos/play?videoId=${args.materialId}")
            .build()
        exoPlayer.setMediaItem(item)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.release()
    }

}