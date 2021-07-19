package com.kartikcd.api.models.response


import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.Article

data class ArticlesResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("articlesCount")
    val articlesCount: Int
)