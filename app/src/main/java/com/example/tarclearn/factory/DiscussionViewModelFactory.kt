package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.DiscussionRepository
import com.example.tarclearn.repository.MessageRepository
import com.example.tarclearn.viewmodel.discussion.DiscussionViewModel
import com.example.tarclearn.viewmodel.discussion.ManageDiscussionViewModel

class DiscussionViewModelFactory(
    private val repository: DiscussionRepository,
    private val mRepository: MessageRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionViewModel::class.java)) {
            return DiscussionViewModel(repository, mRepository) as T
        }
        if (modelClass.isAssignableFrom(ManageDiscussionViewModel::class.java)) {
            return ManageDiscussionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}