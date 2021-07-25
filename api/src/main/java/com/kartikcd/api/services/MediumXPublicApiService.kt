package com.kartikcd.api.services

import com.kartikcd.api.models.response.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MediumXPublicApiService {
    @GET("articles")
    suspend fun getArticles(): Response<ArticlesResponse>

    @GET("articles")
    suspend fun getArticles(@Query("limit") limit: Int, @Query("offset") offset: Int): Response<ArticlesResponse>
}