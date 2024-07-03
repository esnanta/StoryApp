package com.esnanta.storyapp.data.source.remote.api

import com.esnanta.storyapp.data.source.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse


//    @GET("users/{username}")
//    @Headers("Authorization: token ${BuildConfig.TOKEN_GITHUB}")
//    fun getDetail(@Path("username") username: String): Call<UserResponse>
//
//    @GET("users/{username}/followers")
//    @Headers("Authorization: token ${BuildConfig.TOKEN_GITHUB}")
//    fun getListFollower(@Path("username") username: String): Call<List<FollowerResponseItem>>
//
//    @GET("users/{username}/following")
//    @Headers("Authorization: token ${BuildConfig.TOKEN_GITHUB}")
//    fun getListFollowing(@Path("username") username: String): Call<List<FollowingResponseItem>>
}
