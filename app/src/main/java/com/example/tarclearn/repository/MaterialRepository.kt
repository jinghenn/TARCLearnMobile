package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.MaterialDetailDto
import okhttp3.MultipartBody
import retrofit2.Response

class MaterialRepository {
    suspend fun getMaterial(materialId: Int): Response<MaterialDetailDto> {
        return RetrofitInstance.materialApi.getMaterial(materialId)
    }

    suspend fun upload(
        fileModel: MultipartBody.Part,
        filePart: MultipartBody.Part,
        chapterId: Int,
        type: String
    ): Response<MaterialDetailDto> {
        return RetrofitInstance.materialApi.upload(fileModel, filePart, chapterId, type)
    }

    suspend fun updateMaterial(materialId: Int, newMaterial: MaterialDetailDto):Response<MaterialDetailDto>{
        return RetrofitInstance.materialApi.updateMaterial(materialId, newMaterial)
    }
    suspend fun deleteMaterial(materialId: Int):Response<MaterialDetailDto>{
        return RetrofitInstance.materialApi.deleteMaterial(materialId)
    }
}