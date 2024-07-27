package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class ListStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    val listStory: LiveData<PagingData<ListStoryItem>> = repository.getListStory().cachedIn(viewModelScope).asLiveData()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> = _dialogMessage

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }
}