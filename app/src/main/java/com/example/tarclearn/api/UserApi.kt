package com.example.tarclearn.api

import com.example.tarclearn.model.CourseDto
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.model.UserDetailDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("api/users/{id}")//get a particular user
    suspend fun getUser(@Path("id") id: String): Response<UserDetailDto>

    @GET("api/users/{id}/courses")//Get the courses enrolled by a user
    suspend fun getUserCourses(@Path("id") id: String): Response<List<CourseDto>>

    @GET("api/users/{id}/discussions")
    suspend fun getUserDiscussions(@Path("id") id: String): Response<List<DiscussionThreadDto>>
}