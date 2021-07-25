package com.kartikcd.mediumx.ui.feed

import android.text.Layout
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kartikcd.api.models.entities.Article
import com.kartikcd.mediumx.databinding.ListFeedLayoutBinding
import com.kartikcd.mediumx.extensions.loadImage
import com.kartikcd.mediumx.extensions.timeStamp

class FeedListAdapter: PagingDataAdapter<Article, FeedListAdapter.FeedViewHolder>(callback) {

    private var onArticleClickListener: ((Article) -> Unit) ?= null

    companion object {
        private val callback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.slug == newItem.slug
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = ListFeedLayoutBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun setOnArticleClickListener(listener: (Article) -> Unit) {
        onArticleClickListener = listener
    }


    inner class FeedViewHolder(
            val binding: ListFeedLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.usernameTextView.text = article.author.username
            binding.titleTextView.text = article.title
            binding.bodyTextView.text = article.body
            if (Patterns.WEB_URL.matcher(article.author.image).matches()) {
                binding.avatarImageView.loadImage(article.author.image, true)
            } else {
                binding.avatarImageView.loadImage("https://static.productionready.io/images/smiley-cyrus.jpg", true)
            }
            binding.dateTextView.timeStamp = article.createdAt

            binding.root.setOnClickListener {
                onArticleClickListener?.let {
                    it(article)
                }
            }
        }
    }
}