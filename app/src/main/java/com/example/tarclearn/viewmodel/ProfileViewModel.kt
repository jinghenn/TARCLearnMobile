package com.example.tarclearn.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tarclearn.model.User

class ProfileViewModel(
    private val _user: User
): ViewModel() {
   val user get() = _user
    fun isLecturer(): String{
        return user.isLecturer.toString()
    }
}