package com.example.tarclearn.util

class Constants {


    companion object {
        const val BASE_URL = "http://192.168.0.72:44349/"
        const val MODE_CREATE = 1
        const val MODE_EDIT = 2
        const val VIDEO_MATERIAL = "video"
        const val OTHER_MATERIAL = "material"
        val MODE_LIST = listOf("Lecture", "Tutorial", "Practical", "Other")
        const val REQUEST_PICK_MATERIAL = 1
        const val REQUEST_CREATE_FILE = 2
    }
}