package com.kartikcd.api.models.response


import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.User

data class UserResponse(
    @SerializedName("user")
    val user: User
)