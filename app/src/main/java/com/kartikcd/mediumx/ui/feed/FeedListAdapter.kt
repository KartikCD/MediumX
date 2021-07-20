package com.kartikcd.mediumx.ui.feed

import android.text.Layout
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kartikcd.api.models.entities.Article
import com.kartikcd.mediumx.databinding.ListFeedLayoutBinding
import com.kartikcd.mediumx.extensions.loadImage

class FeedListAdapter: RecyclerView.Adapter<FeedListAdapter.FeedViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.slug == newItem.slug
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = ListFeedLayoutBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return FeedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
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
        }
    }
}