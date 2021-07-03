package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.CourseDto
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.model.UserDetailDto
import retrofit2.Response

class UserRepository {
    suspend fun getUserById(id: String): Response<UserDetailDto> {
        return RetrofitInstance.userApi.getUser(id)
    }

    suspend fun getUserCourses(userId: String): Response<List<CourseDto>> {
        return RetrofitInstance.userApi.getUserCourses(userId)
    }

    suspend fun getUserDiscussions(userId: String): Response<List<DiscussionThreadDto>> {
        return RetrofitInstance.userApi.getUserDiscussions(userId)
    }

}