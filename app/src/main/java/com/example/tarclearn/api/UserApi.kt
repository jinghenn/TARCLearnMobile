package com.example.tarclearn.api

import com.example.tarclearn.model.CourseDto
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.model.UserDetailDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("users/{id}")//get a particular user
    suspend fun getUser(@Path("id") id: String): Response<UserDetailDto>

    @GET("users/{id}/courses")//Get the courses enrolled by a user
    suspend fun getUserCourses(@Path("id") id: String): Response<List<CourseDto>>

    @GET("users/{id}/discussions")
    suspend fun getUserDiscussions(@Path("id") id: String): Response<List<DiscussionThreadDto>>
//    @POST("users")//add a new user
//    suspend fun createUser(@Body user: User): Response<User>
//
//    @PUT("users/{id}")//update an existing user
//    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>
//
//    @DELETE("users/{id}")//remove an user
//    suspend fun deleteUser(@Path("id") id: String): Response<User>
}