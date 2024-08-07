package com.esnanta.storyapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AddStoryResponse(
	@SerializedName("error")
	val error: Boolean? = null,

	@SerializedName("message")
	val message: String? = null,
)