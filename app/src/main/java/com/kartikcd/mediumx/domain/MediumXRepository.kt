package com.kartikcd.mediumx.domain

import com.kartikcd.api.MediumXClient
import com.kartikcd.api.models.db.DBArticle
import com.kartikcd.api.models.requests.ArticleRequest
import com.kartikcd.api.models.requests.LoginRequest
import com.kartikcd.api.models.requests.SignupRequest
import com.kartikcd.api.models.requests.UserRequest
import com.kartikcd.api.models.response.ArticleResponse
import com.kartikcd.api.models.response.ArticlesResponse
import com.kartikcd.api.models.response.UserResponse
import com.kartikcd.mediumx.data.local.ArticleDAO
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MediumXRepository {

    private val publicApi = MediumXClient.PUBLIC_API;
    private val authApi = MediumXClient.authApi

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

    suspend fun registerUser(signupRequest: SignupRequest): Resource<UserResponse> {
        val user = authApi.signupUser(signupRequest)
        if (user.code() == 200) {
            user.body()?.let {
                MediumXClient.authToken = it.user.token
                return Resource.Success(it)
            }
        } else if (user.code() == 422) {
            return Resource.Error("Username or Email address is already taken.")
        }
        return Resource.Error("Cannot send request. Server issue!")
    }

    suspend fun createArticle(articleRequest: ArticleRequest): Resource<ArticleResponse> {
        val article = authApi.createArticle(articleRequest)
        if (article.code() == 200) {
            article.body()?.let {
                return Resource.Success(it)
            }
        } else {
            return Resource.Error("Something went wrong.")
        }
        return Resource.Error("Something went wrong.")
    }

    suspend fun loginUser(loginRequest: LoginRequest): Resource<UserResponse> {
        val user = authApi.loginUser(loginRequest)
        if (user.code() == 200) {
            user.body()?.let {
                MediumXClient.authToken = it.user.token
                return Resource.Success(it)
            }
        } else if (user.code() == 422) {
            return Resource.Error("Incorrect username or password.")
        }
        return Resource.Error("Cannot send request. Server issue!")
    }

    suspend fun getCurrentUser(token: String): Response<UserResponse> {
        MediumXClient.authToken = token
        return authApi.getCurrentUser()
    }

    suspend fun getLoggedInUser(): Resource<UserResponse> {
        val response = authApi.getCurrentUser()
        if (response.code() == 200) {
            response.body()?.let {
                return Resource.Success(it)
            }
        } else {
            return Resource.Error("Something went wrong.")
        }
        return Resource.Error("Something went wrong.")
    }

    suspend fun updateUserData(userRequest: UserRequest): Resource<UserResponse> {
        val response = authApi.updateUser(userRequest)
        if (response.code() == 200) {
            response.body()?.let {
                return Resource.Success(it)
            }
        } else {
            return Resource.Error("Something went wrong.")
        }
        return Resource.Error("Something went wrong.")
    }

    suspend fun getArticles(limit: Int, offset: Int): Resource<ArticlesResponse> {
        val articles = publicApi.getArticles(limit, offset)
        if (articles.isSuccessful) {
            articles.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error("Something went wrong.")
    }
}