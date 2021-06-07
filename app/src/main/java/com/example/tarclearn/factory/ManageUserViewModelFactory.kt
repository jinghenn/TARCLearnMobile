package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.CourseRepository
import com.example.tarclearn.repository.UserRepository
import com.example.tarclearn.viewmodel.course.ManageUserViewModel

class ManageUserViewModelFactory(
    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageUserViewModel::class.java)) {
            return ManageUserViewModel(userRepository, courseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}