package com.kartikcd.api.models.requests


import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.SignupData

data class SignupRequest(
    @SerializedName("user")
    val signupData: SignupData
)