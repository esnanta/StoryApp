package com.esnanta.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.esnanta.storyapp.data.source.local.UserPreference
import java.io.File
import com.esnanta.storyapp.data.source.remote.api.ApiService
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.AddStoryResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UploadRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun uploadImage(imageFile: File, description: String): Result<AddStoryResponse> {
        return try {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val successResponse = apiService.addStory(multipartBody, requestBody)
            Result.Success(successResponse)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            Result.Error(errorResponse.message ?: "Unknown error")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        @Volatile
        private var instance: UploadRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ) =
            instance ?: synchronized(this) {
                instance ?: UploadRepository(userPreference, apiService)
            }.also { instance = it }
    }
}