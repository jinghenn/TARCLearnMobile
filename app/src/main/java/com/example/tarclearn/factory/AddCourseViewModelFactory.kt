package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.Repository
import com.example.tarclearn.viewmodel.AddCourseViewModel
import com.example.tarclearn.viewmodel.CourseInfoViewModel

class AddCourseViewModelFactory (
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCourseViewModel::class.java)) {
            return AddCourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}