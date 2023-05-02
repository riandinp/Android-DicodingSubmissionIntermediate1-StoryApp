package com.dicoding.storyapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.view.login.LoginViewModel
import com.dicoding.storyapp.view.main.MainViewModel
import com.dicoding.storyapp.view.welcome.WelcomeViewModel

class ViewModelFactory(private val pref: UserPreference, private val repository: StoryRepository? = null) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }

            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(pref) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref, repository as StoryRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}