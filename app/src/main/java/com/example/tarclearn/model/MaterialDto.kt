package com.example.tarclearn.model

data class MaterialDto(
    val materialId: Int,
    val index: Int,
    val isVideo: Boolean,
    val materialTitle: String
)

data class MaterialDetailDto(
    val materialId: Int,
    //val index: Int,
    val materialTitle: String,
    val materialDescription: String,
    val mode: String,
    val isVideo: Boolean
)