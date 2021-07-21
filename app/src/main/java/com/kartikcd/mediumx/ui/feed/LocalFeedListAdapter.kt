package com.kartikcd.mediumx.ui.feed

import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kartikcd.api.models.db.DBArticle
import com.kartikcd.api.models.entities.Article
import com.kartikcd.mediumx.databinding.ListFeedLayoutBinding
import com.kartikcd.mediumx.extensions.loadImage
import com.kartikcd.mediumx.extensions.timeStamp

class LocalFeedListAdapter: RecyclerView.Adapter<LocalFeedListAdapter.LocalFeedViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<DBArticle>() {
        override fun areItemsTheSame(oldItem: DBArticle, newItem: DBArticle): Boolean {
            return oldItem.slug == newItem.slug
        }

        override fun areContentsTheSame(oldItem: DBArticle, newItem: DBArticle): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    inner class LocalFeedViewHolder(
        val binding: ListFeedLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(dbArticle: DBArticle) {
            binding.apply {
                usernameTextView.text = dbArticle.username
                titleTextView.text = dbArticle.title
                if (Patterns.WEB_URL.matcher(dbArticle.image).matches()) {
                    avatarImageView.loadImage(dbArticle.image, true)
                } else {
                    avatarImageView.loadImage("https://static.productionready.io/images/smiley-cyrus.jpg", true)
                }
                dateTextView.timeStamp = dbArticle.createdAt
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalFeedViewHolder {
        val binding = ListFeedLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return LocalFeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocalFeedViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}