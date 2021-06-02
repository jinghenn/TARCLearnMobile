package com.example.tarclearn.model

data class MaterialDto(
    val materialId: Int,
    val index: Int,
    val isVideo: Boolean,
    val materialTitle: String
)

data class MaterialDetailDto(
    val materialId: Int,
    val index: Int,
    val materialTitle: String,
    val materialDescription: String,
    val materialName: String,
    val mode: String,
    val isVideo: Boolean
) {
    override fun toString(): String {
        return "{\"index\":${index.toString()}," +
                "\"materialTitle\":\"$materialTitle\"," +
                "\"materialDescription\":\"$materialDescription\"," +
                "\"mode\":\"$mode\"," +
                "\"isVideo\":${isVideo.toString()}}"
    }
}