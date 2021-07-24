package com.kartikcd.api.models.response

import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.Article

data class ArticleResponse(
    @SerializedName("article")
    val article: Article
)
