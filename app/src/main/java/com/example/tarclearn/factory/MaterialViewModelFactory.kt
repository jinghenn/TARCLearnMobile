package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.viewmodel.VideoViewModel

class MaterialViewModelFactory(
    private val repository: MaterialRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            return VideoViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}