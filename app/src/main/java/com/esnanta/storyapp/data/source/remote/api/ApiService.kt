package com.esnanta.storyapp.data.source.remote.api

import com.esnanta.storyapp.data.source.remote.response.ListStoryResponse
import com.esnanta.storyapp.data.source.remote.response.LoginResponse
import com.esnanta.storyapp.data.source.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getListStories(): ListStoryResponse
}
