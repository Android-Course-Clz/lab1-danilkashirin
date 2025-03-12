package com.example.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.R
import com.example.adapter.PostsAdapter
import com.example.databinding.ActivityProfileBinding
import com.example.model.Post
import com.example.model.UserProfile
import com.example.utils.DataGenerator
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: PostsAdapter
    private var currentPosts = listOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadData()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = PostsAdapter(
            onCommentAdded = { postId, comment ->
                currentPosts = currentPosts.map { post ->
                    if (post.id == postId) post.copy(
                        comments = post.comments + 1,
                        commentsList = (post.commentsList + comment).toMutableList()
                    ) else post
                }
                adapter.submitList(currentPosts)
            },
            onLikeClicked = { postId ->
                currentPosts = currentPosts.map { post ->
                    if (post.id == postId) post.toggleLike() else post
                }
                adapter.submitList(currentPosts)
            }
        )

        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = this@ProfileActivity.adapter
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            currentPosts = DataGenerator.generateRandomPosts(3)
            adapter.submitList(currentPosts)

            val user = UserProfile.getDefault().copy(
                postsCount = currentPosts.size,
                followers = 15,
                following = 8
            )
            updateUI(user)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: UserProfile) {
        Glide.with(this)
            .load(user.avatarUrl)
            .placeholder(R.drawable.ic_profile_placeholder)
            .into(binding.avatarImageView)

        with(binding) {
            nameTextView.text = user.name
            usernameTextView.text = user.username
            followersCountTextView.text = user.followers.toString()
            followingCountTextView.text = user.following.toString()
            postsCountTextView.text = user.postsCount.toString()
        }
    }
}