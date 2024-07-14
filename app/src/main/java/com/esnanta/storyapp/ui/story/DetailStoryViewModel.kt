package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.DetailStoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _storyDetail = MutableLiveData<Result<DetailStoryResponse>>()
    val storyDetail: LiveData<Result<DetailStoryResponse>> get() = _storyDetail

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> = _dialogMessage

    fun fetchStoryDetail(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                repository.getStoryDetail(id)
            }
            when (result) {
                is Result.Loading -> _storyDetail.postValue(Result.Loading)
                is Result.Success -> _storyDetail.postValue(Result.Success(result.data))
                is Result.Error -> _storyDetail.postValue(Result.Error(result.error))
            }
            _isLoading.value = false
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }
}