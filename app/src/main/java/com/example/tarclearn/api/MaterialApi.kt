package com.example.tarclearn.api

import com.example.tarclearn.model.MaterialDetailDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MaterialApi {
    @GET("api/materials/{id}")
    suspend fun getMaterial(@Path("id") id: Int): Response<MaterialDetailDto>

    @Multipart
    @POST("api/upload")
    suspend fun upload(
        @Part fileModel: MultipartBody.Part,
        @Part filePart: MultipartBody.Part,
        @Query("chapterId") chapterId: Int,
        @Query("type") materialType: String
    ): Response<MaterialDetailDto>

    @DELETE("api/materials/{id}")
    suspend fun deleteMaterial(@Path("id") id: Int): Response<MaterialDetailDto>

    @PUT("api/materials/{id}")
    suspend fun updateMaterial(
        @Path("id") id: Int,
        @Body newMaterial: MaterialDetailDto
    ): Response<MaterialDetailDto>

    @GET("api/materials/exist/")
    suspend fun isFileExist(@Query("materialId") materialId: Int): Response<Boolean>

    @GET("api/materials/index/")
    suspend fun isIndexExist(
        @Query("chapterId") chapterId: Int,
        @Query("materialIndex") materialIndex: Int,
        @Query("mode") mode: String,
        @Query("isVideo") isVideo: Boolean
    ): Response<Boolean>
}