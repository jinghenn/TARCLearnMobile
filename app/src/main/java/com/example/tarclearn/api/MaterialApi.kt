package com.example.tarclearn.api

import com.example.tarclearn.model.MaterialDetailDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MaterialApi {
    @GET("materials/{id}")
    suspend fun getMaterial(@Path("id")id: Int): Response<MaterialDetailDto>


}