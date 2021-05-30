package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.viewmodel.ManageChapterViewModel
import com.example.tarclearn.viewmodel.VideoListViewModel

class ChapterViewModelFactory(
    private val repository: ChapterRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageChapterViewModel::class.java)) {
            return ManageChapterViewModel(repository) as T
        }
        if(modelClass.isAssignableFrom(VideoListViewModel::class.java)) {
            return VideoListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}