package com.example.tarclearn.api

import com.example.tarclearn.model.UserDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface CourseApi {
    @GET("courses/{id}/users")//get a list of users enrolled in a course
    suspend fun getCourseUsers(@Path("id") id: String): Response<List<UserDto>>

    @POST("courses/enrol")
    suspend fun enrol(
        @Query("courseId") courseId: String,
        @Query("userId") userId: String
    ): Response<List<UserDto>>

    @DELETE("courses/unenrol")
    suspend fun unenrol(
        @Query("courseId") courseId: String,
        @Query("userId") userId: String
    ): Response<Void>
}