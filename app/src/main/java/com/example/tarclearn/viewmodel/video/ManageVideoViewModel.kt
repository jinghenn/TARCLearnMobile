package com.example.tarclearn.viewmodel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.MaterialRepository
import kotlinx.coroutines.launch

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

    private val _noExistFlag = MutableLiveData<Boolean?>()
    val noExistFlag: LiveData<Boolean?> get() = _noExistFlag

    fun fetchVideoDetail(materialId: Int) {
        viewModelScope.launch {
            val response = repository.getMaterial(materialId)
            if (response.code() == 200) {
                _video.value = response.body()
            }

        }
    }

    fun updateVideo(materialId: Int, newMaterial: MaterialDetailDto) {
        viewModelScope.launch {
            val response = repository.updateMaterial(materialId, newMaterial)

            if (response.code() == 200) {
                _video.value = response.body()
                _success.value = true
            }
            if (response.code() == 400 || response.code() == 404) {
                _error.value = true
            }
        }
    }

    fun checkIsVideoIndexExist(chapterId: Int, materialNo: Int, mode: String, isVideo: Boolean) {
        viewModelScope.launch {
            val response = repository.isIndexExist(chapterId, materialNo, mode, isVideo)
            if (response.code() == 200) {
                _noExistFlag.value = response.body()
            }
        }
    }

    fun resetSuccessFlag() {
        _success.value = null
    }

    fun resetErrorFlag() {
        _error.value = null
    }

    fun resetNoExistFlag() {
        _noExistFlag.value = null
    }
}