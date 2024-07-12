package com.esnanta.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.esnanta.storyapp.data.repository.UploadRepository
import java.io.File

class AddStoryViewModel (private val repository: UploadRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}