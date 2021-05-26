package com.example.tarclearn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.UserCourseDto
import com.example.tarclearn.model.UserDto
import com.example.tarclearn.repository.Repository
import kotlinx.coroutines.launch

class ManageUserViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _courseList = MutableLiveData<List<UserCourseDto>>()
    val courseList: LiveData<List<UserCourseDto>>
        get() = _courseList

    private val _userList = MutableLiveData<List<UserDto>>()
    val userList: LiveData<List<UserDto>>
        get() = _userList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String>
        get() = _successMessage


    fun fetchCourseList(userId: String) {
        viewModelScope.launch {
            val response = repository.getUserCourses(userId)
            if (response.code() == 200) {
                _courseList.value = response.body()
            }
        }
    }

    fun getCourseListWithName(): List<String> {
        val list = mutableListOf<String>()
        if (_courseList.value != null) {
            for (x in _courseList.value!!) {
                val str = "${x.courseId} ${x.courseTitle}"
                list.add(str)
            }
        }
        return list
    }

    fun fetchUserList(courseId: String) {
        viewModelScope.launch {
            val response = repository.getCourseUsers(courseId)
            if (response.code() == 200) {
                _userList.value = response.body()
            }
        }
    }

    fun enrol(courseId: String, userId: String) {
        viewModelScope.launch {
            val response = repository.enrol(courseId, userId)
            if (response.code() == 201) {
                _userList.value = response.body()
                _successMessage.value = "User: $userId added to Course: $courseId"
            }
            if (response.code() == 404) {
                _errorMessage.value = "User: $userId does not exist"
            }
            if (response.code() == 409) {
                _errorMessage.value = "User: $userId already in Course: $courseId."
            }

        }
    }
}