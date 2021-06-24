package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.CourseDetailDto
import com.example.tarclearn.model.UserDto
import retrofit2.Response

class CourseRepository {
    suspend fun getCourseUsers(courseId: Int): Response<List<UserDto>> {
        return RetrofitInstance.courseApi.getCourseUsers(courseId)
    }

    suspend fun createCourse(course: CourseDetailDto): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.createCourse(course)
    }

    suspend fun getCourseInfo(courseId: Int): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.getCourseInfo(courseId)
    }

    suspend fun enrol(courseId: Int, emailList: List<String>): Response<List<String>> {
        return RetrofitInstance.courseApi.enrol(courseId, emailList)
    }

    suspend fun unenrol(courseId: Int, userId: String): Response<Void> {
        return RetrofitInstance.courseApi.unenrol(courseId, userId)
    }

    suspend fun editCourse(courseId: Int, course: CourseDetailDto): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.editCourse(courseId, course)
    }

    suspend fun deleteCourse(courseId: Int): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.deleteCourse(courseId)
    }

    suspend fun getCourseChapters(courseId: Int): Response<List<ChapterDetailDto>> {
        return RetrofitInstance.courseApi.getCourseChapters(courseId)
    }
}