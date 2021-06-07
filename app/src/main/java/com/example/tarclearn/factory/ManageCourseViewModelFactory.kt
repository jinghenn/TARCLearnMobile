package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.CourseRepository
import com.example.tarclearn.viewmodel.course.ManageCourseViewModel

class ManageCourseViewModelFactory (
    private val repository: CourseRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageCourseViewModel::class.java)) {
            return ManageCourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}