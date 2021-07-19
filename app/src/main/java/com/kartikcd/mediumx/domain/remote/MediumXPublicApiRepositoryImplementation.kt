package com.kartikcd.mediumx.domain.remote

import com.kartikcd.api.MediumXClient
import com.kartikcd.api.models.response.ArticlesResponse
import retrofit2.Response

class MediumXPublicApiRepositoryImplementation: MediumXPublicApiRepository {
    override suspend fun getArticles(): Response<ArticlesResponse> {
        return MediumXClient.publicApi.getArticles();
    }

}