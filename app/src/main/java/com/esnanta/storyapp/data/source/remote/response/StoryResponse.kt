package com.esnanta.storyapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(
	@SerializedName("error")
	val error: Boolean? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("listStory")
	val listStory: List<Story>? = null
)

data class Story(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	val lon: Double? = null,
	val id: String? = null,
	val lat: Double? = null
)