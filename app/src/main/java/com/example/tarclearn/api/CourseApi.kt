package com.example.tarclearn.api

import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.CourseDetailDto
import com.example.tarclearn.model.UserDto
import retrofit2.Response
import retrofit2.http.*

interface CourseApi {
    @GET("api/courses/{id}/users")//get a list of users enrolled in a course
    suspend fun getCourseUsers(@Path("id") id: Int): Response<List<UserDto>>

    @POST("api/courses/{id}/enrol")
    suspend fun enrol(@Path("id") courseId: Int, @Body emailList: List<String>)
            : Response<List<String>>

    @PUT("api/courses/{id}/unenrol")
    suspend fun unenrol(
        @Path("id") courseId: Int,
        @Body emailList: List<String>,
        @Query("requesterEmail") requesterEmail: String
    ): Response<List<String>>

//    @DELETE("courses/unenrol")
//    suspend fun unenrol(
//        @Query("courseId") courseId: Int,
//        @Query("userId") userId: String
//    ): Response<Void>

    @GET("api/courses/{id}")
    suspend fun getCourseInfo(@Path("id") id: Int): Response<CourseDetailDto>

    @POST("api/courses")
    suspend fun createCourse(@Body course: CourseDetailDto): Response<CourseDetailDto>

    @PUT("api/courses/{id}")
    suspend fun editCourse(
        @Path("id") id: Int,
        @Body course: CourseDetailDto
    ): Response<CourseDetailDto>

    @DELETE("api/courses/{id}")
    suspend fun deleteCourse(@Path("id") id: Int): Response<CourseDetailDto>

    @GET("api/courses/{id}/chapters")
    suspend fun getCourseChapters(@Path("id") id: Int): Response<List<ChapterDetailDto>>
}