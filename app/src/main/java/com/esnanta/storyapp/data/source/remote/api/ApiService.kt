package com.esnanta.storyapp.data.source.remote.api

import com.esnanta.storyapp.data.source.remote.response.AddStoryResponse
import com.esnanta.storyapp.data.source.remote.response.DetailStoryResponse
import com.esnanta.storyapp.data.source.remote.response.ListStoryResponse
import com.esnanta.storyapp.data.source.remote.response.LoginResponse
import com.esnanta.storyapp.data.source.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
    suspend fun getListStory(): ListStoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(@Path("id") id: String): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
        //@Part("lat") lat: RequestBody?,
        //@Part("lon") lon: RequestBody?
    ): AddStoryResponse
}
