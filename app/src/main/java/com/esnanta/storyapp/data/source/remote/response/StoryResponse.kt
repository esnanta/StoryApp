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
