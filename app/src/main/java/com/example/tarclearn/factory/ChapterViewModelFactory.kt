package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.viewmodel.course.ManageChapterViewModel
import com.example.tarclearn.viewmodel.discussion.DiscussionListViewModel
import com.example.tarclearn.viewmodel.video.VideoListViewModel
import com.example.tarclearn.viewmodel.material.MaterialListViewModel
import com.example.tarclearn.viewmodel.quiz.QuizListViewModel

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
        if (modelClass.isAssignableFrom(MaterialListViewModel::class.java)) {
            return MaterialListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DiscussionListViewModel::class.java)) {
            return DiscussionListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(QuizListViewModel::class.java)) {
            return QuizListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}