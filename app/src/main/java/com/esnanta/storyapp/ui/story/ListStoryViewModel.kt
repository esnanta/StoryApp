package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _listStory = MutableLiveData<Result<List<ListStoryItem>>>()
    val listStory: LiveData<Result<List<ListStoryItem>>> get() = _listStory

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> = _dialogMessage

    fun fetchListStory() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                repository.getListStory()
            }
            when (result) {
                is Result.Loading -> _listStory.postValue(Result.Loading)
                is Result.Success -> _listStory.postValue(Result.Success(result.data.listStory))
                is Result.Error -> _listStory.postValue(Result.Error(result.error))
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }
}