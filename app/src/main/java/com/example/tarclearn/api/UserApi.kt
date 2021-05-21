package com.example.tarclearn.api

import com.example.tarclearn.model.User
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("users/{id}")//get a particular user
    suspend fun getUser(@Path("id") id: String) : Response<User>

    @POST("users")//add a new user
    suspend fun createUser(@Body user: User): Response<User>

    @PUT("users/{id}")//update an existing user
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>

    @DELETE("users/{id}")//remove an user
    suspend fun deleteUser(@Path("id") id: String): Response<User>
}