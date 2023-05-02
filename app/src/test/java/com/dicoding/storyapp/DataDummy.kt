package com.dicoding.storyapp

import com.dicoding.storyapp.data.remote.response.StoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<StoryItem> {
        val items: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItem(
                id = "story-ID-$i",
                name = "name-$i",
                photoUrl = "https://i.pinimg.com/564x/c5/c6/2c/c5c62cc1281e039822f200b8bee3c44b.jpg",
                createdAt = "2023-05-02T17:10:50.107Z",
                description = "description-$i",
                lat = 1.0,
                lon = 2.0
            )
            items.add(story)
        }
        return items
    }
}