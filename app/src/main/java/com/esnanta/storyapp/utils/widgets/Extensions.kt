package com.esnanta.storyapp.utils.widgets

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.esnanta.storyapp.data.source.local.entity.ListStoryEntity
import com.esnanta.storyapp.data.source.remote.response.ListStoryItem

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}

fun ListStoryEntity.toListStoryItem(): ListStoryItem {
    return ListStoryItem(
        id = this.id,
        photoUrl = this.photoUrl,
        createdAt = this.createdAt,
        name = this.name,
        description = this.description,
        lon = this.lon,
        lat = this.lat
    )
}
