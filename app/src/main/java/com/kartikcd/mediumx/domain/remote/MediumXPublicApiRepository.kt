package com.kartikcd.mediumx.domain.remote

import com.kartikcd.api.models.entities.Article
import com.kartikcd.api.models.response.ArticlesResponse
import retrofit2.Response

interface MediumXPublicApiRepository {
    suspend fun getArticles(): Response<ArticlesResponse>;

}