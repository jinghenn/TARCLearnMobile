package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.ChapterDto
import com.example.tarclearn.model.CourseDetailDto
import com.example.tarclearn.model.UserDto
import retrofit2.Response

class CourseRepository {
    suspend fun getCourseUsers(courseId: String): Response<List<UserDto>> {
        return RetrofitInstance.courseApi.getCourseUsers(courseId)
    }

    suspend fun createCourse(course: CourseDetailDto): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.createCourse(course)
    }

    suspend fun getCourseInfo(courseId: String): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.getCourseInfo(courseId)
    }

    suspend fun enrol(courseId: String, userId: String): Response<List<UserDto>> {
        return RetrofitInstance.courseApi.enrol(courseId, userId)
    }

    suspend fun unenrol(courseId: String, userId: String): Response<Void> {
        return RetrofitInstance.courseApi.unenrol(courseId, userId)
    }

    suspend fun editCourse(courseId: String, course: CourseDetailDto): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.editCourse(courseId, course)
    }

    suspend fun deleteCourse(courseId: String): Response<CourseDetailDto> {
        return RetrofitInstance.courseApi.deleteCourse(courseId)
    }
    suspend fun getCourseChapters(courseId: String): Response<List<ChapterDetailDto>> {
        return RetrofitInstance.courseApi.getCourseChapters(courseId)
    }
}