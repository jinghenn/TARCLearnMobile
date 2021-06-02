package com.example.tarclearn.api

import com.example.tarclearn.model.MaterialDetailDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MaterialApi {
    @GET("materials/{id}")
    suspend fun getMaterial(@Path("id") id: Int): Response<MaterialDetailDto>

    @Multipart
    @POST("upload")
    suspend fun upload(
        @Part fileModel: MultipartBody.Part,
        @Part filePart: MultipartBody.Part,
        @Query("chapterId") chapterId: Int,
        @Query("type") materialType: String
    ): Response<MaterialDetailDto>

    @DELETE("materials/{id}")
    suspend fun deleteMaterial(@Path("id") id: Int): Response<MaterialDetailDto>

    @PUT("materials/{id}")
    suspend fun updateMaterial(
        @Path("id") id: Int,
        @Body newMaterial: MaterialDetailDto
    ): Response<MaterialDetailDto>
}