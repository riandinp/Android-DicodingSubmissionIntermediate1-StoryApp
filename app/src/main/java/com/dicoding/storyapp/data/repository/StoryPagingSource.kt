package com.dicoding.storyapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.data.remote.network.DicodingApiService
import com.dicoding.storyapp.data.remote.response.StoryItem

class StoryPagingSource(private val apiService: DicodingApiService) :
    PagingSource<Int, StoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position, params.loadSize, 1)
            LoadResult.Page(
                data = responseData.listStory ?: emptyList(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}