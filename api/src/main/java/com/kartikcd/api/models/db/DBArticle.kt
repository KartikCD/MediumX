package com.kartikcd.api.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.Author
import java.sql.RowId

@Entity(
    tableName = "articles"
)
data class DBArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("slug")
    val slug: String,
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
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("image")
    val image: String
)
