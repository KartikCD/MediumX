package com.kartikcd.api.models.entities


import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("bio")
    val bio: String,
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("image")
    val image: String,
    @SerializedName("username")
    val username: String
)