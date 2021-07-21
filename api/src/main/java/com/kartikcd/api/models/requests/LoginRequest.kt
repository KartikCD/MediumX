package com.kartikcd.api.models.requests


import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.LoginData

data class LoginRequest(
    @SerializedName("user")
    val loginData: LoginData
)