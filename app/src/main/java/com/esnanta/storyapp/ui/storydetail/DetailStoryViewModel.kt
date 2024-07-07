package com.esnanta.storyapp.ui.storydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.DetailStoryResponse

class DetailStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStoryDetail(id: String): LiveData<Result<DetailStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = repository.getStoryDetail(id)
            response.collect { emit(it) }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}
