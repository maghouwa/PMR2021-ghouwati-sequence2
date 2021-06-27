package com.App1.app1.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val description: String,
    @SerializedName("checked")
    var fait: Int,
    @SerializedName("url")
    val url: String
)