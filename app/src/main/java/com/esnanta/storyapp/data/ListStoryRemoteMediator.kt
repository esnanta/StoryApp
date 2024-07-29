package com.esnanta.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.esnanta.storyapp.data.source.local.StoryDatabase
import com.esnanta.storyapp.data.source.local.entity.ListStoryEntity
import com.esnanta.storyapp.data.source.remote.api.ApiService

@OptIn(ExperimentalPagingApi::class)
class ListStoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, ListStoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryEntity>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        try {
            val responseData = apiService.getListStoryWithPage(page, state.config.pageSize)
            val endOfPaginationReached = responseData.listStory.isEmpty()

            // Convert ListStoryItem to ListStoryEntity
            val stories = responseData.listStory.map { storyItem ->
                ListStoryEntity(
                    id = storyItem.id.toString(),
                    photoUrl = storyItem.photoUrl,
                    createdAt = storyItem.createdAt,
                    name = storyItem.name,
                    description = storyItem.description,
                    lon = storyItem.lon,
                    lat = storyItem.lat
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.listStoryDao().deleteAllStories()
                }
                database.listStoryDao().insert(stories)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}