package com.kartikcd.mediumx.domain

import com.kartikcd.api.MediumXClient
import com.kartikcd.api.models.db.DBArticle
import com.kartikcd.api.models.entities.Article
import com.kartikcd.api.models.response.ArticlesResponse
import com.kartikcd.mediumx.data.local.ArticleDAO
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.flow.Flow

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

    suspend fun saveArticlesToLocalDatabase(dbArticle: DBArticle, articleDAO: ArticleDAO) {
        articleDAO.insert(dbArticle)
    }

    fun getSavedArticle(articleDAO: ArticleDAO): Flow<List<DBArticle>> {
        return articleDAO.getAllArticles()
    }

    suspend fun deleteSavedArticle(dbArticle: DBArticle, articleDAO: ArticleDAO) {
        articleDAO.deleteArticle(dbArticle)
    }
}