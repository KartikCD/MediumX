package com.kartikcd.mediumx.domain

import com.kartikcd.api.MediumXClient
import com.kartikcd.api.models.response.ArticlesResponse
import com.kartikcd.mediumx.util.Resource

class MediumXRepository {

    private val publicApi = MediumXClient.publicApi;

    suspend fun getArticles(): Resource<ArticlesResponse> {
        val articles = publicApi.getArticles()
        if (articles.isSuccessful) {
            articles.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(articles.message())
    }
}