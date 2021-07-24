package com.kartikcd.api.models.requests

import com.google.gson.annotations.SerializedName
import com.kartikcd.api.models.entities.Update

data class UserRequest(
    @SerializedName("user")
    val user: Update
)