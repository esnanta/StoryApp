package com.esnanta.storyapp

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem

class StoryPagingSource(private val items: List<ListStoryItem>) : PagingSource<Int, ListStoryItem>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return LoadResult.Page(
            data = items,
            prevKey = null,
            nextKey = null
        )
    }
}