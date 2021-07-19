package com.kartikcd.mediumx.domain

import com.kartikcd.api.models.response.ArticlesResponse
import com.kartikcd.mediumx.domain.remote.MediumXPublicApiRepository
import com.kartikcd.mediumx.util.Resource
import retrofit2.Response

class MediumXRepositoryImpl(
    private val mediumXPublicApiRepository: MediumXPublicApiRepository
): MediumXRepository {
    override suspend fun getArticles(): Resource<ArticlesResponse> {
        TODO("Not yet implemented")
    }

    private fun responseToResource(response: Response<ArticlesResponse>):Resource<ArticlesResponse>{
        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}