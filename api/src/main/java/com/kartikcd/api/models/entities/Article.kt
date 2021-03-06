package com.kartikcd.api.models.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author")
    val author: Author,
    @SerializedName("body")
    val body: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("favorited")
    val favorited: Boolean,
    @SerializedName("favoritesCount")
    val favoritesCount: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("tagList")
    val tagList: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)