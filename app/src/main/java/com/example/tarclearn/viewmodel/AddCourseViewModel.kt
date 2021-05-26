package com.example.tarclearn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.CourseDetailDto
import com.example.tarclearn.repository.Repository
import kotlinx.coroutines.launch

class AddCourseViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _course = MutableLiveData<CourseDetailDto>()
    val course: LiveData<CourseDetailDto>
        get() = _course

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _success = MutableLiveData<Boolean?>()
    val success: LiveData<Boolean?>
        get() = _success

    fun createCourse(userId: String, courseId: String, courseName: String, desc: String) {
        viewModelScope.launch {
            val newCourse = CourseDetailDto(courseId, courseName, desc)
            val response = repository.createCourse(newCourse)
            if (response.code() == 201) {
                _course.value = response.body()
                _error.value = null
                _success.value = true
                repository.enrol(courseId, userId)
            }
            if (response.code() == 409) {
                _error.value = "Course: $courseId already exist"
                _success.value = null
            }
            if (response.code() == 400) {
                _error.value = "Error creating course. Please check your input"
                _success.value = null
            }
        }
    }
    fun editCourse(courseId: String, courseName: String, desc: String) {
        viewModelScope.launch {
            val newCourse = CourseDetailDto(courseId, courseName, desc)
            val response = repository.editCourse(courseId,newCourse)
            if (response.code() == 200) {
                _course.value = response.body()
                _error.value = null
                _success.value = true
            }
            if (response.code() == 404) {
                _error.value = "Course: $courseId does not exist"
                _success.value = null
            }
            if (response.code() == 400) {
                _error.value = "Error editing course. Please check your input"
                _success.value = null
            }
        }
    }

    fun resetSuccessFlag(){
        _success.value = null
    }
}