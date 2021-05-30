package com.example.tarclearn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.MaterialDto
import com.example.tarclearn.repository.ChapterRepository
import kotlinx.coroutines.launch

class VideoListViewModel(
    private val repository: ChapterRepository
) : ViewModel() {
    private val _videoList = MutableLiveData<List<MaterialDto>>()
    val chapterList: LiveData<List<MaterialDto>>
        get() = _videoList

    fun fetchVideoList(chapterId: Int){
        viewModelScope.launch {
            val response = repository.getChapterVideos(chapterId)
            if(response.code() == 200){
                _videoList.value = response.body()
            }

        }
    }
}