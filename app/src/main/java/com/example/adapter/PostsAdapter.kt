package com.example.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.R
import com.example.model.Post
import com.example.utils.PostsDiffUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class PostsAdapter(
    private val onCommentAdded: (Int, String) -> Unit,
    private val onLikeClicked: (Int) -> Unit
) : ListAdapter<Post, PostsAdapter.PostViewHolder>(PostsDiffUtil()) {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postText: TextView = itemView.findViewById(R.id.postTextTextView)
        private val postImage: ImageView = itemView.findViewById(R.id.postImageView)
        private val likeButton: MaterialButton = itemView.findViewById(R.id.likeButton)
        private val commentButton: MaterialButton = itemView.findViewById(R.id.commentButton)
        private val commentsPreview: TextView = itemView.findViewById(R.id.commentsPreview)
        private val commentsContainer: LinearLayout = itemView.findViewById(R.id.commentsContainer)

        @SuppressLint("SetTextI18n")
        fun bind(post: Post) {
            postText.text = post.text
            val textSecondaryColor =
                ContextCompat.getColor(itemView.context, R.color.text_secondary)

            commentButton.iconTint = ColorStateList.valueOf(textSecondaryColor)
            commentButton.setTextColor(textSecondaryColor)
            commentButton.text = post.comments.toString()

            Glide.with(itemView)
                .load(post.imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(postImage)

            updateLikeButton(post)
            updateCommentsPreview(post)

            likeButton.setOnClickListener { onLikeClicked(post.id) }
            commentButton.setOnClickListener { showCommentDialog(post.id) }
        }

        @SuppressLint("SetTextI18n")
        private fun updateLikeButton(post: Post) {
            val iconRes =
                if (post.isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outline
            val tintColor = ContextCompat.getColor(
                itemView.context,
                if (post.isLiked) R.color.red else R.color.text_secondary
            )

            likeButton.icon = ContextCompat.getDrawable(itemView.context, iconRes)
            likeButton.iconTint = ColorStateList.valueOf(tintColor)
            likeButton.text = post.likes.toString()
        }

        @SuppressLint("SetTextI18n")
        private fun updateCommentsPreview(post: Post) {
            if (post.commentsList.isNotEmpty()) {
                commentsPreview.text = "Last comment: ${post.commentsList.last()}"
                commentsContainer.visibility = View.VISIBLE
                commentsContainer.animate().alpha(1f).setDuration(300).start()
            } else {
                commentsContainer.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction { commentsContainer.visibility = View.GONE }
                    .start()
            }
        }

        private fun showCommentDialog(postId: Int) {
            val context = itemView.context

            val dialog = Dialog(context).apply {
                setContentView(R.layout.dialog_comment)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                window?.setLayout(
                    (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                window?.setWindowAnimations(R.style.DialogAnimation)
            }

            val input = dialog.findViewById<TextInputEditText>(R.id.commentInput)
            val inputLayout = dialog.findViewById<TextInputLayout>(R.id.inputLayout)

            dialog.findViewById<MaterialButton>(R.id.submitButton).apply {
                iconTint = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary))
                setOnClickListener {
                    val comment = input.text.toString().trim()
                    if (comment.isNotBlank()) {
                        onCommentAdded(postId, comment)
                        dialog.dismiss()
                    } else {
                        inputLayout.error = "Comment cannot be empty"
                    }
                }
            }

            dialog.findViewById<MaterialButton>(R.id.cancelButton).apply {
                setTextColor(ContextCompat.getColor(context, R.color.primary))
                setOnClickListener { dialog.dismiss() }
            }

            dialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}