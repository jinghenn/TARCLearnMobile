package com.example.tarclearn.viewmodel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.ChapterRepository
import kotlinx.coroutines.launch

class VideoListViewModel(
    private val repository: ChapterRepository
) : ViewModel() {
    private val _videoList = MutableLiveData<List<MaterialDetailDto>>()
    val chapterList: LiveData<List<MaterialDetailDto>>
        get() = _videoList

    private var _mode = 0
    val mode get() = _mode

    fun fetchVideoList(chapterId: Int, mode: String) {
        viewModelScope.launch {
            val response = repository.getChapterVideos(chapterId, mode)
            if (response.code() == 200) {
                _videoList.value = response.body()
            }

        }
    }
    fun changeMode(mode: Int){
        _mode = mode
    }
}