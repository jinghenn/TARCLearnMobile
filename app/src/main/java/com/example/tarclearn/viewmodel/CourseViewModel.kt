package com.example.tarclearn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.UserCourseDto
import com.example.tarclearn.repository.Repository
import kotlinx.coroutines.launch

class CourseViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _courseList = MutableLiveData<List<UserCourseDto>>()
    val courseList: LiveData<List<UserCourseDto>>
        get() = _courseList

fun fetchCourseList(userId: String) {
    viewModelScope.launch {
        val response = repository.getUserCourses(userId)
        if (response.code() == 200) {
            _courseList.value = response.body()
        }
    }
}


}