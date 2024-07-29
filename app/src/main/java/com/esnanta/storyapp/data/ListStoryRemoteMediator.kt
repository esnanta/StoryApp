package com.esnanta.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.esnanta.storyapp.data.source.local.StoryDatabase
import com.esnanta.storyapp.data.source.local.entity.ListStoryEntity
import com.esnanta.storyapp.data.source.local.entity.RemoteKeysEntity
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
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

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
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.listStoryDao().deleteAllStories()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.listStory.map {
                    RemoteKeysEntity(id = it.id.toString(), prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insert(keys)
                database.listStoryDao().insert(stories)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}