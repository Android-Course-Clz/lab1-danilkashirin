package com.example.utils

import com.example.model.Post
import kotlin.random.Random

object DataGenerator {
    private val sampleTexts = listOf(
        "Text 1",
        "Text 2",
        "Text 3"
    )

    private val fixedImageUrls = listOf(
        "https://images.unsplash.com/photo-1724430943574-23348700dfea",
        "https://images.unsplash.com/photo-1739467444239-840b9b3c2480",
        "https://images.unsplash.com/photo-1740484687035-6b8cbf14aa64"
    )

    fun generateRandomPosts(count: Int): List<Post> {
        return (1..count).map { id ->
            Post(
                id = id,
                text = sampleTexts[(id - 1) % sampleTexts.size],
                imageUrl = fixedImageUrls[(id - 1) % fixedImageUrls.size],
                likes = Random.nextInt(100, 1000),
                comments = Random.nextInt(10, 500),
                isLiked = false,
                commentsList = mutableListOf()
            )
        }
    }
}
