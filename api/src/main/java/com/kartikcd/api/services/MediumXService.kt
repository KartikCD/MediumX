package com.kartikcd.api.services

import com.kartikcd.api.models.response.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MediumXService {

    @GET("articles")
    suspend fun getArticles(): Response<ArticlesResponse>
}