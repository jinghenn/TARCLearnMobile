package com.example.tarclearn.viewmodel.material

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.ChapterRepository
import kotlinx.coroutines.launch

class MaterialListViewModel(
    private val repository: ChapterRepository
) : ViewModel() {
    private val _materialList = MutableLiveData<List<MaterialDetailDto>>()
    val materialList: LiveData<List<MaterialDetailDto>>
        get() = _materialList

    fun fetchMaterialList(chapterId: Int, mode: String) {
        viewModelScope.launch {
            val response = repository.getChapterMaterials(chapterId, mode)
            if (response.code() == 200) {
                _materialList.value = response.body()
            }
        }
    }
}