package com.esnanta.storyapp.data.repository

import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.api.ApiService
import com.esnanta.storyapp.data.source.remote.response.AddStoryResponse
import com.esnanta.storyapp.data.source.remote.response.DetailStoryResponse
import com.esnanta.storyapp.data.source.remote.response.ListStoryResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) : IRepository {

    suspend fun getListStory(): Result<ListStoryResponse> {
        return try {
            val response = apiService.getListStory()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun getStoryDetail(id: String): Result<DetailStoryResponse> {
        return try {
            val response = apiService.getStoryDetail(id)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

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
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService)
            }.also { instance = it }
    }
}