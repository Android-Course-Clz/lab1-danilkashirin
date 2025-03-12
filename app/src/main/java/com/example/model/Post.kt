package com.example.model

data class Post(
    val id: Int,
    val text: String,
    val imageUrl: String?,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean,
    val commentsList: MutableList<String>
) {
    fun toggleLike() = copy(
        isLiked = !isLiked,
        likes = if (isLiked) likes - 1 else likes + 1
    )
}
