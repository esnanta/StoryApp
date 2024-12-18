package com.esnanta.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.esnanta.storyapp.data.ListStoryRemoteMediator
import com.esnanta.storyapp.data.source.local.StoryDatabase
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.utils.widgets.toListStoryItem
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.api.ApiService
import com.esnanta.storyapp.data.source.remote.response.AddStoryResponse
import com.esnanta.storyapp.data.source.remote.response.DetailStoryResponse
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem
import com.esnanta.storyapp.data.source.remote.response.ListStoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

open class StoryRepository protected constructor(
    private val storyDatabase : StoryDatabase,
    private val userPreference: UserPreference,
    private val apiService: ApiService
)  {

    open suspend fun logout() {
        userPreference.logout()
    }

    @OptIn(ExperimentalPagingApi::class)
    open fun getListStory(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = ListStoryRemoteMediator(storyDatabase,apiService),
            pagingSourceFactory = {
                storyDatabase.listStoryDao().getAllStories()
            }
        ).flow.map { pagingData->
            pagingData.map { listStoryEntity->
                listStoryEntity.toListStoryItem()
            }
        }
    }

    open suspend fun getStoryDetail(id: String): Result<DetailStoryResponse> {
        return try {
            val response = apiService.getStoryDetail(id)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    open suspend fun uploadImage(imageFile: File, description: String,
                                 latitude: Double? = null, longitude: Double? = null)
    : Result<AddStoryResponse> {

        return try {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            val lat = latitude?.toString()?.toRequestBody("text/plain".toMediaType())
            val lon = longitude?.toString()?.toRequestBody("text/plain".toMediaType())

            val successResponse = apiService.addStory(multipartBody, requestBody, lat, lon)
            Result.Success(successResponse)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            Result.Error(errorResponse.message ?: "Unknown error")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    open suspend fun getStoriesWithLocation(): Result<ListStoryResponse> {
        return try {
            val response = apiService.getStoriesWithLocation()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase,userPreference, apiService)
            }.also { instance = it }
    }
}