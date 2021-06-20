package com.example.tarclearn.viewmodel.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.CourseDetailDto
import com.example.tarclearn.repository.CourseRepository
import kotlinx.coroutines.launch

class CourseInfoViewModel(
    private val repository: CourseRepository
) : ViewModel() {
    private val _course = MutableLiveData<CourseDetailDto>()
    val course: LiveData<CourseDetailDto>
        get() = _course

    private val _chapterList = MutableLiveData<List<ChapterDetailDto>>()
    val chapterList: LiveData<List<ChapterDetailDto>>
        get() = _chapterList

    private val _success = MutableLiveData<Boolean?>()
    val success: LiveData<Boolean?>
        get() = _success

    fun fetchCourseInfo(courseId: Int) {
        viewModelScope.launch {
            val response = repository.getCourseInfo(courseId)
            if (response.code() == 200) {
                _course.value = response.body()
            }
        }
    }

    fun fetchChapterList(courseId: Int) {
        viewModelScope.launch {
            val response = repository.getCourseChapters(courseId)
            if (response.code() == 200) {
                _chapterList.value = response.body()
            }
        }
    }

    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            val response = repository.deleteCourse(courseId)
            if (response.code() == 200) {
                _success.value = true
            }
        }
    }


    fun resetSuccessFlag() {
        _success.value = null
    }

}