package com.kartikcd.api.models.requests

import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.ArticleBody

data class ArticleRequest(
    @SerializedName("article")
    val article: ArticleBody
)
