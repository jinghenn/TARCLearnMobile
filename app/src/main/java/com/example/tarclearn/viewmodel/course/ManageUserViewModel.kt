package com.example.tarclearn.viewmodel.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.CourseDto
import com.example.tarclearn.model.UserDto
import com.example.tarclearn.repository.CourseRepository
import com.example.tarclearn.repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ManageUserViewModel(
    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _courseList = MutableLiveData<List<CourseDto>>()
    val courseList: LiveData<List<CourseDto>>
        get() = _courseList

    private val _userList = MutableLiveData<List<UserDto>>()
    val userList: LiveData<List<UserDto>>
        get() = _userList

    private val _failedEmailList = MutableLiveData<List<String>>()
    val failedEmailList: LiveData<List<String>> get() = _failedEmailList

    private val _emailList = MutableLiveData<List<String>>()
    val emailList: LiveData<List<String>> get() = _emailList

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String>
        get() = _successMessage


    fun fetchCourseList(userId: String) {
        viewModelScope.launch {
            val response = userRepository.getUserCourses(userId)
            if (response.code() == 200) {
                _courseList.value = response.body()
            }
        }
    }


    fun fetchUserList(courseId: Int) {
        viewModelScope.launch {
            val response = courseRepository.getCourseUsers(courseId)
            if (response.code() == 200) {
                _userList.value = response.body()
            }
        }
    }

    //    fun enrol(courseId: Int, userId: String) {
//        viewModelScope.launch {
//            val response = courseRepository.enrol(courseId, userId)
//            if (response.code() == 201) {
//                _userList.value = response.body()
//                _successMessage.value = "User: $userId added successfully"
//            }
//            if (response.code() == 404) {
//                _errorMessage.value = "User: $userId does not exist"
//            }
//            if (response.code() == 409) {
//                _errorMessage.value = "User: $userId is already in this course."
//            }
//
//        }
//    }
    fun enrol(courseId: Int, emailList: List<String>) {

        viewModelScope.launch {
            val response = courseRepository.enrol(courseId, emailList)
            if (response.code() == 200) {
                _emailList.value = response.body()
                _successMessage.value = "User(s) added successfully"
            }
            if (response.code() == 404) {
                val rawResponse = response.errorBody()?.string()

                val gson = Gson()
                val list = gson.fromJson(rawResponse, Array<String>::class.java).toList()

                _failedEmailList.value = list
            }

        }
    }

    fun validateEmailList(raw: String): List<String> {
        val splitted = raw.split(",").toMutableList()
        val emailIterator = splitted.listIterator()

        while (emailIterator.hasNext()) {

            val currentEmail = emailIterator.next()
            if (currentEmail.isBlank()) {
                emailIterator.remove()
                continue
            }
            emailIterator.set(currentEmail.trim())
        }
        return splitted
    }
}