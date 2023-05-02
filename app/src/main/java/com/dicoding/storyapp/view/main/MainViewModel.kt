package com.dicoding.storyapp.view.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.remote.response.StoryItem
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.di.Injection

class MainViewModel(repository: StoryRepository) : ViewModel() {

    private lateinit var preferences: UserPreference
    val listStory: LiveData<PagingData<StoryItem>> =
        repository.getStories().cachedIn(viewModelScope)


    class ViewModelFactory(private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(Injection.provideRepository(context)) as T
                }

                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
}