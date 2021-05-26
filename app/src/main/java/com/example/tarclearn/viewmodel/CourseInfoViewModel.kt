package com.example.tarclearn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.CourseDetailDto
import com.example.tarclearn.repository.Repository
import kotlinx.coroutines.launch

class CourseInfoViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _course = MutableLiveData<CourseDetailDto>()
    val course: LiveData<CourseDetailDto>
        get() = _course

    private val _success = MutableLiveData<Boolean?>()
    val success: LiveData<Boolean?>
        get() = _success

    fun fetchCourseInfo(courseId: String) {
        viewModelScope.launch {
            val response = repository.getCourseInfo(courseId)
            if (response.code() == 200) {
                _course.value = response.body()
            }
        }
    }
    fun deleteCourse(courseId: String){
        viewModelScope.launch {
            val response = repository.deleteCourse(courseId)
            if(response.code() == 200){
                _success.value = true
            }
        }
    }
    fun resetSuccessFlag(){
        _success.value = null
    }

}