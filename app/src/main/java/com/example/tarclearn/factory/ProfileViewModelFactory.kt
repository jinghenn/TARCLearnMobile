package com.example.tarclearn.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.model.User
import com.example.tarclearn.viewmodel.ProfileViewModel
import java.lang.IllegalArgumentException

class ProfileViewModelFactory(
    private val user: User
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}