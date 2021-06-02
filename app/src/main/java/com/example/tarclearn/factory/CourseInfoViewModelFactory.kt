package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.CourseRepository
import com.example.tarclearn.repository.UserRepository
import com.example.tarclearn.viewmodel.CourseInfoViewModel

class CourseInfoViewModelFactory(
    private val repository: CourseRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseInfoViewModel::class.java)) {
            return CourseInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}