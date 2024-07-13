package com.esnanta.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.esnanta.storyapp.data.repository.UploadRepository
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.AddStoryResponse
import java.io.File

class AddStoryViewModel(private val repository: UploadRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<Result<AddStoryResponse>>()
    val uploadResult: LiveData<Result<AddStoryResponse>> = _uploadResult

    fun uploadImage(file: File, description: String) = liveData {
        emit(Result.Loading)
        val result = repository.uploadImage(file, description)
        emitSource(result)
    }
}