package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.response.StoryItem
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.di.Injection
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference, repository: StoryRepository) : ViewModel() {

    val listStory: LiveData<PagingData<StoryItem>> = repository.getStories().cachedIn(viewModelScope)

    class ViewModelFactory(private val pref: UserPreference) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(pref, Injection.provideRepository()) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}