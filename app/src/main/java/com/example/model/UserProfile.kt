package com.example.model

data class UserProfile(
    val avatarUrl: String,
    val name: String,
    val username: String,
    val followers: Int,
    val following: Int,
    val postsCount: Int
) {
    companion object {
        fun getDefault() = UserProfile(
            avatarUrl = "https://images.unsplash.com/photo-1739312023925-19eca8ca00aa",
            name = "Default User",
            username = "@user",
            followers = 0,
            following = 0,
            postsCount = 0
        )
    }
}
