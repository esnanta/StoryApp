package com.esnanta.storyapp.data

import com.esnanta.storyapp.data.model.UserModel
import com.esnanta.storyapp.data.source.remote.api.ApiService
import com.esnanta.storyapp.data.source.remote.response.RegisterResponse
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.remote.Result
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun registerSession(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "An unknown error occurred")
        }
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}