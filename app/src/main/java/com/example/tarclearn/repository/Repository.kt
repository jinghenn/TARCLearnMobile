package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.UserCourseDto
import com.example.tarclearn.model.UserDetailDto
import com.example.tarclearn.model.UserDto
import retrofit2.Response

class Repository {
    suspend fun getUserById(id: String): Response<UserDetailDto> {
        return RetrofitInstance.userApi.getUser(id)
    }

    suspend fun getUserCourses(userId: String): Response<List<UserCourseDto>> {
        return RetrofitInstance.userApi.getUserCourses(userId)
    }

    suspend fun getCourseUsers(courseId: String): Response<List<UserDto>> {
        return RetrofitInstance.courseApi.getCourseUsers(courseId)
    }

    suspend fun enrol(courseId: String, userId: String): Response<List<UserDto>> {
        return RetrofitInstance.courseApi.enrol(courseId, userId)
    }

    suspend fun unenrol(courseId: String, userId: String): Response<Void> {
        return RetrofitInstance.courseApi.unenrol(courseId, userId)
    }
//    suspend fun createUser(user: User): Response<User> {
//        return RetrofitInstance.userApi.createUser(user)
//    }
//    suspend fun updateUser(id: String, user: User): Response<User> {
//        return RetrofitInstance.userApi.updateUser(id, user)
//    }
//    suspend fun deleteUser(id: String): Response<User> {
//        return RetrofitInstance.userApi.deleteUser(id)
//    }

}