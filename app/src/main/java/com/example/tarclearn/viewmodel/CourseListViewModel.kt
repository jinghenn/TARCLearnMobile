package com.example.tarclearn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.CourseDto
import com.example.tarclearn.repository.UserRepository
import kotlinx.coroutines.launch

class CourseListViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _courseList = MutableLiveData<List<CourseDto>>()
    val courseList: LiveData<List<CourseDto>>
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