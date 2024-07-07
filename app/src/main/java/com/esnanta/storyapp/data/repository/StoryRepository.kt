package com.esnanta.storyapp.data.repository

import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.remote.api.ApiService
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.response.DetailStoryResponse
import com.esnanta.storyapp.data.source.remote.response.ListStoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) : IRepository {

    fun getListStory(): Flow<Result<ListStoryResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getListStory()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getStoryDetail(id: String): Flow<Result<DetailStoryResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryDetail(id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
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