package com.example.tarclearn.viewmodel.material

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.MaterialRepository
import kotlinx.coroutines.launch

class MaterialViewModel(val repository: MaterialRepository) : ViewModel() {
    private val _material = MutableLiveData<MaterialDetailDto>()
    val material: LiveData<MaterialDetailDto>
        get() = _material

    private val _fileAvailable = MutableLiveData<Boolean?>()
    val fileAvailable: LiveData<Boolean?> get() = _fileAvailable

    private val _uri  = MutableLiveData<Uri>()
    val uri: LiveData<Uri> get() = _uri

    fun fetchMaterialDetail(materialId: Int) {
        viewModelScope.launch {
            val response = repository.getMaterial(materialId)
            if (response.code() == 200) {
                _material.value = response.body()
            }
        }
    }

    fun checkFileAvailability(materialId: Int) {
        viewModelScope.launch {
            val response = repository.isFileExist(materialId)
            if (response.code() == 200) {
                _fileAvailable.value = response.body()
            } else {
                _fileAvailable.value = null
            }

        }
    }

    fun setDownloadCompleted(uri: Uri){
        _uri.value = uri
    }
}