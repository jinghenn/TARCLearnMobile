package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.MaterialDetailDto
import retrofit2.Response

class MaterialRepository {
    suspend fun getMaterial(materialId: Int): Response<MaterialDetailDto> {
        return RetrofitInstance.materialApi.getMaterial(materialId)
    }

    suspend fun isFileExist(materialId: Int): Response<Boolean> {
        return RetrofitInstance.materialApi.isFileExist(materialId)
    }

    suspend fun isIndexExist(chapterId: Int, materialIndex: Int, mode: String, isVideo: Boolean): Response<Boolean> {
        return RetrofitInstance.materialApi.isIndexExist(chapterId, materialIndex, mode, isVideo)
    }

    suspend fun updateMaterial(
        materialId: Int,
        newMaterial: MaterialDetailDto
    ): Response<MaterialDetailDto> {
        return RetrofitInstance.materialApi.updateMaterial(materialId, newMaterial)
    }

    suspend fun deleteMaterial(materialId: Int): Response<MaterialDetailDto> {
        return RetrofitInstance.materialApi.deleteMaterial(materialId)
    }
}