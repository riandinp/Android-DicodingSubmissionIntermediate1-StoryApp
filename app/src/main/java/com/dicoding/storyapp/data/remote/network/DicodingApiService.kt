package com.dicoding.storyapp.data.remote.network

import com.dicoding.storyapp.data.remote.payload.LoginPayload
import com.dicoding.storyapp.data.remote.payload.RegisterPayload
import com.dicoding.storyapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface DicodingApiService {

    @GET("/v1/stories")
    suspend fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): GetStoriesResponse

    @GET("/v1/stories")
    fun getStoriesWithLocation(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 1
    ): Call<GetStoriesResponse>

    @GET("/v1/stories/{userId}")
    fun getDetailStory(
        @Path("userId") userId: String
    ): Call<GetDetailStoryResponse>

    @Multipart
    @POST("/v1/stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<PostStoryResponse>


    @Headers("No-Authentication: true")
    @POST("/v1/login")
    fun login(
        @Body payload: LoginPayload
    ): Call<LoginResponse>

    @Headers("No-Authentication: true")
    @POST("/v1/register")
    fun register(
        @Body payload: RegisterPayload
    ): Call<RegisterResponse>
}