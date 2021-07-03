package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.repository.UserRepository
import com.example.tarclearn.viewmodel.course.CourseListViewModel
import com.example.tarclearn.viewmodel.discussion.MyDiscussionsViewModel
import com.example.tarclearn.viewmodel.login.LoginViewModel

class UserViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MyDiscussionsViewModel::class.java)) {
            return MyDiscussionsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CourseListViewModel::class.java)) {
            return CourseListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}