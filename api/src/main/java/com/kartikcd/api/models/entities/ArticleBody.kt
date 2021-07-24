package com.kartikcd.api.models.entities


import com.google.gson.annotations.SerializedName

data class ArticleBody(
    @SerializedName("body")
    val body: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("tagList")
    val tagList: List<String>,
    @SerializedName("title")
    val title: String
)