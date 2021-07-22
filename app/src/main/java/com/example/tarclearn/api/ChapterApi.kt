package com.example.tarclearn.api

import com.example.tarclearn.model.*
import retrofit2.Response
import retrofit2.http.*

interface ChapterApi {
    @POST("api/chapters")
    suspend fun createChapter(
        @Query("courseId") courseId: Int,
        @Body newChapter: ChapterDto
    ): Response<ChapterDto>

    @DELETE("api/chapters")
    suspend fun deleteChapter(@Query("chapterId") id: Int): Response<ChapterDetailDto>

    @GET("api/chapters/{id}/videos")
    suspend fun getChapterVideos(
        @Path("id") id: Int,
        @Query("mode") mode: String
    ): Response<List<MaterialDetailDto>>

    @GET("api/chapters/{id}/materials")
    suspend fun getChapterMaterials(
        @Path("id") id: Int,
        @Query("mode") mode: String
    ): Response<List<MaterialDetailDto>>

    @PUT("api/chapters/{id}")
    suspend fun updateChapter(
        @Path("id") id: Int,
        @Body updatedChapter: ChapterDto
    ): Response<ChapterDto>

    @GET("api/chapters/{id}")
    suspend fun getChapter(@Path("id") id: Int): Response<ChapterDto>

    @GET("api/chapters/{id}/discussions")
    suspend fun getChapterDiscussions(@Path("id") id: Int): Response<List<DiscussionThreadDto>>

    @GET("api/chapters/{id}/quiz")
    suspend fun getChapterQuizzes(@Path("id") chapterId: Int): Response<List<QuizDto>>

    @GET("api/chapters/no")
    suspend fun isChapterNoExist(
        @Query("chapterNo") chapterNo: String,
        @Query("courseId") courseId: Int
    ): Response<Boolean>
}