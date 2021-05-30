package com.example.tarclearn.api

import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.ChapterDto
import com.example.tarclearn.model.MaterialDto
import retrofit2.Response
import retrofit2.http.*

interface ChapterApi {
    @POST("chapters")
    suspend fun createChapter(
        @Query("courseId") courseId: String,
        @Body newChapter: ChapterDto
    ): Response<ChapterDto>

    @DELETE("chapters")
    suspend fun deleteChapter(@Query("chapterId") id: Int): Response<ChapterDetailDto>

    @GET("chapters/{id}/vids")
    suspend fun getChapterVideos(@Path("id") id: Int): Response<List<MaterialDto>>

    @PUT("chapters/{id}")
    suspend fun updateChapter(@Path("id") id: Int, @Body updatedChapter: ChapterDto): Response<ChapterDto>
    @GET("chapters/{id}")
    suspend fun getChapter(@Path("id") id: Int,): Response<ChapterDto>
}