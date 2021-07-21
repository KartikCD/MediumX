package com.kartikcd.api.services

import com.kartikcd.api.models.requests.LoginRequest
import com.kartikcd.api.models.requests.SignupRequest
import com.kartikcd.api.models.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MediumXAuthApiService {

    @POST("users")
    suspend fun signupUser(@Body signupRequest: SignupRequest): Response<UserResponse>

    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<UserResponse>

}