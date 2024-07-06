package com.esnanta.storyapp.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.Result

class ListStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getListStory() = liveData {
        emit(Result.Loading)
        try {
            val response = repository.getListStory()
            response.collect { emit(it) }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}