package com.example.tarclearn.api

import com.example.tarclearn.model.*
import retrofit2.Response
import retrofit2.http.*

interface ChapterApi {
    @POST("chapters")
    suspend fun createChapter(
        @Query("courseId") courseId: Int,
        @Body newChapter: ChapterDto
    ): Response<ChapterDto>

    @DELETE("chapters")
    suspend fun deleteChapter(@Query("chapterId") id: Int): Response<ChapterDetailDto>

    @GET("chapters/{id}/videos")
    suspend fun getChapterVideos(
        @Path("id") id: Int,
        @Query("mode") mode: String
    ): Response<List<MaterialDetailDto>>

    @GET("chapters/{id}/materials")
    suspend fun getChapterMaterials(
        @Path("id") id: Int,
        @Query("mode") mode: String
    ): Response<List<MaterialDetailDto>>

    @PUT("chapters/{id}")
    suspend fun updateChapter(
        @Path("id") id: Int,
        @Body updatedChapter: ChapterDto
    ): Response<ChapterDto>

    @GET("chapters/{id}")
    suspend fun getChapter(@Path("id") id: Int): Response<ChapterDto>

    @GET("chapters/{id}/discussions")
    suspend fun getChapterDiscussions(@Path("id") id: Int): Response<List<DiscussionThreadDto>>

    @GET("chapters/{id}/quiz")
    suspend fun getChapterQuizzes(@Path("id") chapterId: Int): Response<List<QuizDto>>
}