package com.esnanta.storyapp


import com.esnanta.storyapp.data.source.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyListStoryItem(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val item = ListStoryItem(
                photoUrl = "https://dummyimage.com/600x400/000/fff&text=Photo+$i",
                createdAt = "2023-01-01T12:00:00Z",
                name = "Author $i",
                description = "Description $i",
                lon = -122.0840,
                id = i.toString(),
                lat = 37.4219983
            )
            items.add(item)
        }
        return items
    }
}