package com.example.tarclearn.viewmodel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.model.MaterialDto
import com.example.tarclearn.repository.MaterialRepository
import kotlinx.coroutines.launch

class VideoViewModel(
    private val repository: MaterialRepository
) : ViewModel() {
    private val _video = MutableLiveData<MaterialDetailDto>()
    val video: LiveData<MaterialDetailDto>
        get() = _video

    fun fetchVideoDetail(materialId: Int) {
        viewModelScope.launch {
            val response = repository.getMaterial(materialId)
            if (response.code() == 200) {
                _video.value = response.body()
            }

        }
    }
}