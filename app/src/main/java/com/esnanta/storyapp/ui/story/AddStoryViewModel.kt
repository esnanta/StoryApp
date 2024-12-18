package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.R
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.AddStoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<Result<AddStoryResponse>>()
    val uploadResult: LiveData<Result<AddStoryResponse>> get() = _uploadResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> get() = _dialogMessage

    fun uploadImage(file: File, description: String, latitude: Double? = null, longitude: Double? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _uploadResult.postValue(Result.Loading)
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.uploadImage(file, description, latitude, longitude)
                }
                _uploadResult.postValue(result)
                if (result is Result.Success) {
                    _dialogMessage.postValue(result.data.message)
                } else if (result is Result.Error) {
                    _dialogMessage.postValue(result.error)
                }
            } catch (e: Exception) {
                _uploadResult.postValue(Result.Error(e.message ?: R.string.unknown_error.toString()))
                _dialogMessage.postValue(e.message ?: R.string.unknown_error.toString())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }
}