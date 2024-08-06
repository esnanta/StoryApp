package com.esnanta.storyapp.ui.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.esnanta.storyapp.DataDummy
import com.esnanta.storyapp.StoryPagingSource
import com.esnanta.storyapp.data.model.UserModel
import com.esnanta.storyapp.data.repository.StoryRepository
import com.esnanta.storyapp.data.source.local.StoryDatabase
import com.esnanta.storyapp.data.source.local.UserPreference
import com.esnanta.storyapp.data.source.remote.Result
import com.esnanta.storyapp.data.source.remote.api.ApiService
import com.esnanta.storyapp.data.source.remote.response.DetailStoryItem
import com.esnanta.storyapp.data.source.remote.response.DetailStoryResponse
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem
import com.esnanta.storyapp.data.source.remote.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

open class FakeStoryRepository(
    storyDatabase: StoryDatabase,
    userPreference: UserPreference,
    apiService: ApiService
) : StoryRepository(storyDatabase, userPreference, apiService) {

    private val dummyStories = DataDummy.generateDummyListStoryItem()

    override fun getSession(): Flow<UserModel> {
        return flow {
            emit(UserModel(email = "test@test.com", token = "token", isLogin = true))
        }
    }

    override suspend fun logout() {
        // Do nothing for logout in fake repository
    }

    override fun getListStory(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(dummyStories) }
        ).flow
    }

    override suspend fun getStoryDetail(id: String): Result<DetailStoryResponse> {
        val story = dummyStories.find { it.id == id }
        return if (story != null) {
            Result.Success(
                DetailStoryResponse(
                    error = false,
                    message = "Success",
                    detailStoryItem = DetailStoryItem(
                        id = story.id,
                        photoUrl = story.photoUrl,
                        createdAt = story.createdAt,
                        name = story.name,
                        description = story.description,
                        lon = story.lon,
                        lat = story.lat
                    )
                )
            )
        } else {
            Result.Error("Story not found")
        }
    }

    override suspend fun uploadImage(
        imageFile: File, description: String, latitude: Double?, longitude: Double?
    ): Result<StoryResponse> {
        return Result.Success(StoryResponse(error = false, message = "Success"))
    }

    override suspend fun getStoriesWithLocation(): Result<StoryResponse> {
        return Result.Success(StoryResponse(error = false, message = "Success"))
    }
}