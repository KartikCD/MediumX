package com.kartikcd.api.services

import com.kartikcd.api.models.requests.ArticleRequest
import com.kartikcd.api.models.requests.LoginRequest
import com.kartikcd.api.models.requests.SignupRequest
import com.kartikcd.api.models.requests.UserRequest
import com.kartikcd.api.models.response.ArticleResponse
import com.kartikcd.api.models.response.ArticlesResponse
import com.kartikcd.api.models.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MediumXAuthApiService {

    @POST("users")
    suspend fun signupUser(@Body signupRequest: SignupRequest): Response<UserResponse>

    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<UserResponse>

    @GET("user")
    suspend fun getCurrentUser(): Response<UserResponse>

    @PUT("user")
    suspend fun updateUser(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("articles")
    suspend fun createArticle(@Body articleRequest: ArticleRequest): Response<ArticleResponse>

    @GET("articles")
    suspend fun getMyArticles(): Response<ArticlesResponse>
}