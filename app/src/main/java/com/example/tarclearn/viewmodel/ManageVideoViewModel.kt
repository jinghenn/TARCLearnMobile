package com.example.tarclearn.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.util.Constants
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ManageVideoViewModel(
    private val repository: MaterialRepository
) : ViewModel() {
    private val _video = MutableLiveData<MaterialDetailDto>()
    val video: LiveData<MaterialDetailDto>
        get() = _video

    private val _success = MutableLiveData<Boolean?>()
    val success: LiveData<Boolean?> get() = _success

    private val _error = MutableLiveData<Boolean?>()
    val error: LiveData<Boolean?> get() = _error

    fun fetchVideoDetail(materialId: Int) {
        viewModelScope.launch {
            val response = repository.getMaterial(materialId)
            if (response.code() == 200) {
                _video.value = response.body()
            }

        }
    }

//    fun uploadVideo(fileModel: MultipartBody.Part, file: MultipartBody.Part, chapterId: Int, type: String) {
//        viewModelScope.launch {
//            val response = repository.upload(fileModel, file, chapterId, Constants.VIDEO_MATERIAL)
//            if(response.code() == 200){
//                _video.value = response.body()
//                _success.value = true
//            }
//            if(response.code() == 400){
//                _error.value = true
//            }
//        }
//    }
    fun updateVideo(materialId: Int, newMaterial: MaterialDetailDto){
        viewModelScope.launch {
            val response = repository.updateMaterial(materialId, newMaterial)

            if(response.code() == 200){
                _video.value = response.body()
                _success.value = true
            }
            if(response.code() == 400 || response.code() == 404){
                _error.value = true
            }
        }
    }
    fun resetSuccessFlag(){
        _success.value = null
    }
    fun resetErrorFlag(){
        _error.value = null
    }
}