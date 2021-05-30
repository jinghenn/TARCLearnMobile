package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.CourseDto
import com.example.tarclearn.model.UserDetailDto
import retrofit2.Response

class UserRepository {
    suspend fun getUserById(id: String): Response<UserDetailDto> {
        return RetrofitInstance.userApi.getUser(id)
    }

    suspend fun getUserCourses(userId: String): Response<List<CourseDto>> {
        return RetrofitInstance.userApi.getUserCourses(userId)
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