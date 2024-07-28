package com.esnanta.storyapp.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ListStoryEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: String,

    @SerializedName("photoUrl")
    val photoUrl: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("lon")
    val lon: Double? = null,

    @SerializedName("lat")
    val lat: Double? = null
) : Parcelable