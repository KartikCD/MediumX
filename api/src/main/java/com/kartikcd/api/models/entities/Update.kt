package com.kartikcd.api.models.entities

import com.google.gson.annotations.SerializedName

data class Update(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("bio")
    var bio: String? = null
)