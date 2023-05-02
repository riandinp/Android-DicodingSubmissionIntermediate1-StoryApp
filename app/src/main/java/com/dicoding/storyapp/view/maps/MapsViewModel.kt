package com.dicoding.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.remote.network.ApiConfig
import com.dicoding.storyapp.data.remote.response.ErrorResponse
import com.dicoding.storyapp.data.remote.response.GetStoriesResponse
import com.dicoding.storyapp.data.remote.response.StoryItem
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {
    private val _listStory = MutableLiveData<List<StoryItem>>()
    val listStory: LiveData<List<StoryItem>> = _listStory

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loadingScreen: LiveData<Boolean> = _loadingScreen

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String> = _snackBarText

    fun getListStory() {
        _loadingScreen.value = true

        val client = ApiConfig.getApiService().getStoriesWithLocation()
        client.enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
            ) {
                _loadingScreen.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _listStory.value = responseBody.listStory ?: emptyList()
                        _snackBarText.value = "Success"
                    }
                } else {
                    val responseBody = response.errorBody()
                    if (responseBody != null) {
                        val mapper =
                            Gson().fromJson(responseBody.string(), ErrorResponse::class.java)
                        _snackBarText.value = mapper.message
                        Log.e(TAG, "onFailure2: ${mapper.message}")
                    } else {
                        _snackBarText.value = response.message()
                        Log.e(TAG, "onFailure2: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                _loadingScreen.value = false
            }

        })
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}