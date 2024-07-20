package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.response.StoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryMapViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _storiesWithLocation = MutableLiveData<Result<StoryResponse>>()
    val storiesWithLocation: LiveData<Result<StoryResponse>> = _storiesWithLocation

    fun fetchStoriesWithLocation() {
        _storiesWithLocation.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = storyRepository.getStoriesWithLocation()
            withContext(Dispatchers.Main) {
                _storiesWithLocation.value = result
            }
        }
    }
}