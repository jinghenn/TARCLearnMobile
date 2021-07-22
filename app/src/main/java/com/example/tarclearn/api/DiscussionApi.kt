package com.example.tarclearn.api

import com.example.tarclearn.model.DiscussionAboutDto
import com.example.tarclearn.model.DiscussionThread
import com.example.tarclearn.model.DiscussionThreadDetailDto
import com.example.tarclearn.model.MessageDetailDto
import retrofit2.Response
import retrofit2.http.*

interface DiscussionApi {
    @POST("api/discussions")
    suspend fun createDiscussionThread(@Body newThread: DiscussionThread):
            Response<DiscussionThreadDetailDto>

    @GET("api/discussions/{id}")
    suspend fun getDiscussionDetail(@Path("id") id: Int): Response<DiscussionThreadDetailDto>

    @PUT("api/discussions/{id}")
    suspend fun updateDiscussionDetail(
        @Path("id") id: Int,
        @Body updatedThread: DiscussionAboutDto
    ): Response<DiscussionAboutDto>

    @DELETE("api/discussions/{id}")
    suspend fun deleteDiscussionThread(@Path("id") id: Int): Response<DiscussionAboutDto>

    @GET("api/discussions/{id}/messages")
    suspend fun getDiscussionMessages(@Path("id") id: Int): Response<List<MessageDetailDto>>
}