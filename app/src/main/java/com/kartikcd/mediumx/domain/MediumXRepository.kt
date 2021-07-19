package com.kartikcd.mediumx.domain

import com.kartikcd.api.models.response.ArticlesResponse
import com.kartikcd.mediumx.util.Resource

interface MediumXRepository {
    suspend fun getArticles(): Resource<ArticlesResponse>
}