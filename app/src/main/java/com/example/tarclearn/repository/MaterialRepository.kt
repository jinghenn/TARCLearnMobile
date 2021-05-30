package com.example.tarclearn.repository

import com.example.tarclearn.api.RetrofitInstance
import com.example.tarclearn.model.MaterialDetailDto
import retrofit2.Response

class MaterialRepository {
    suspend fun getMaterial(materialId: Int):Response<MaterialDetailDto>{
        return RetrofitInstance.materialApi.getMaterial(materialId)
    }
}