package com.example.tarclearn.viewmodel.material

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

    fun fetchMaterialDetail(materialId: Int){
        viewModelScope.launch {
            val response = repository.getMaterial(materialId)
            if(response.code() == 200){
                _material.value = response.body()
            }
        }
    }

}