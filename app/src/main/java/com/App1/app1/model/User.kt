package com.App1.app1.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("pseudo")
    val ipseudo: String,
    @SerializedName("pass")
    val pass: String,
    @SerializedName("hash")
    val hash: String
)