package com.example.tarclearn.api

import com.example.tarclearn.model.DiscussionMessage
import com.example.tarclearn.model.MessageDetailDto
import com.example.tarclearn.model.MessageDto
import retrofit2.Response
import retrofit2.http.*

interface MessageApi {
    @GET("api/messages/{id}")
    suspend fun getMessage(@Path("id") id: Int): Response<MessageDetailDto>

    @PUT("api/messages/{id}")
    suspend fun updateMessage(
        @Path("id") id: Int, @Body updatedMessage: MessageDto
    ): Response<MessageDetailDto>

    @POST("api/messages")
    suspend fun createMessage(@Body newMessage: DiscussionMessage): Response<MessageDetailDto>

    @DELETE("api/messages/{id}")
    suspend fun deleteMessage(@Path("id") id: Int): Response<MessageDetailDto>
}