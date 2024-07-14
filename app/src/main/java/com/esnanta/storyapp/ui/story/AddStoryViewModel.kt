package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esnanta.storyapp.data.repository.UploadRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.AddStoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AddStoryViewModel(private val repository: UploadRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<Result<AddStoryResponse>>()
    val uploadResult: LiveData<Result<AddStoryResponse>> get() = _uploadResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogMessage = MutableLiveData<String?>()
    val dialogMessage: LiveData<String?> = _dialogMessage

    fun uploadImage(file: File, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _uploadResult.postValue(Result.Loading)
            repository.uploadImage(file, description).observeForever { result ->
                when (result) {
                    is Result.Success -> {
                        _uploadResult.postValue(Result.Success(result.data))
                        _dialogMessage.postValue(result.data.message)
                    }
                    is Result.Error -> {
                        _uploadResult.postValue(Result.Error(result.error))
                        _dialogMessage.postValue(result.error)
                    }
                    else -> {
                        _uploadResult.postValue(Result.Error("Unknown error"))
                        _dialogMessage.postValue("Unknown error")
                    }
                }
                _isLoading.value = false
            }
        }
    }

    fun clearDialogMessage() {
        _dialogMessage.value = null
    }
}