package com.example.tarclearn.api

import com.example.tarclearn.model.DiscussionAboutDto
import com.example.tarclearn.model.DiscussionThread
import com.example.tarclearn.model.DiscussionThreadDetailDto
import retrofit2.Response
import retrofit2.http.*

interface DiscussionApi {
    @POST("discussions")
    suspend fun createDiscussionThread(@Body newThread: DiscussionThread):
            Response<DiscussionThreadDetailDto>

    @GET("discussions/{id}")
    suspend fun getDiscussionDetail(@Path("id") id: Int): Response<DiscussionThreadDetailDto>

    @PUT("discussions/{id}")
    suspend fun updateDiscussionDetail(
        @Path("id") id: Int,
        @Body updatedThread: DiscussionAboutDto
    ): Response<DiscussionAboutDto>

    @DELETE("discussions/{id}")
    suspend fun deleteDiscussionThread(@Path("id") id: Int): Response<DiscussionAboutDto>
}