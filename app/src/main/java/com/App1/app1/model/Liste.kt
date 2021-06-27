package com.App1.app1.model

import com.google.gson.annotations.SerializedName

data class Liste(
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val titreListeToDo: String
)