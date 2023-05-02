package com.dicoding.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.local.database.StoryDatabase
import com.dicoding.storyapp.data.remote.network.DicodingApiService
import com.dicoding.storyapp.data.remote.response.StoryItem

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: DicodingApiService
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<StoryItem>> {
        Log.d("TestLog", "Repository Kepanggil")
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false,
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }
}