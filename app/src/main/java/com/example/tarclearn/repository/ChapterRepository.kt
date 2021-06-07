package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.ChapterDto
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.model.MaterialDto
import retrofit2.Response

class ChapterRepository {
    suspend fun createChapter(courseId: String, newChapter: ChapterDto): Response<ChapterDto> {
        return RetrofitInstance.chapterApi.createChapter(courseId, newChapter)
    }
    suspend fun updateChapter(chapterId:Int, updatedChapter: ChapterDto): Response<ChapterDto>{
        return RetrofitInstance.chapterApi.updateChapter(chapterId, updatedChapter)
    }
    suspend fun deleteChapter(chapterId: Int): Response<ChapterDetailDto> {
        return RetrofitInstance.chapterApi.deleteChapter(chapterId)
    }
    suspend fun getChapterVideos(chapterId: Int, mode: String): Response<List<MaterialDetailDto>> {
        return RetrofitInstance.chapterApi.getChapterVideos(chapterId, mode)
    }
    suspend fun getChapterMaterials(chapterId: Int, mode: String): Response<List<MaterialDetailDto>> {
        return RetrofitInstance.chapterApi.getChapterMaterials(chapterId, mode)
    }
    suspend fun getChapter(chapterId: Int): Response<ChapterDto> {
        return RetrofitInstance.chapterApi.getChapter(chapterId)
    }
}