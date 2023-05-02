package com.dicoding.storyapp.di

import com.dicoding.storyapp.data.remote.network.ApiConfig
import com.dicoding.storyapp.data.repository.StoryRepository

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}