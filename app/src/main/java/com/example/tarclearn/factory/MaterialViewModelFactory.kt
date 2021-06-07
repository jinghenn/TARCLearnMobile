package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.viewmodel.material.ManageMaterialViewModel
import com.example.tarclearn.viewmodel.material.MaterialViewModel
import com.example.tarclearn.viewmodel.video.ManageVideoViewModel
import com.example.tarclearn.viewmodel.video.VideoViewModel

class MaterialViewModelFactory(
    private val repository: MaterialRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            return VideoViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ManageVideoViewModel::class.java)) {
            return ManageVideoViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MaterialViewModel::class.java)) {
            return MaterialViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ManageMaterialViewModel::class.java)) {
            return ManageMaterialViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}