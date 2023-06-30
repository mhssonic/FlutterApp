package com.mhssonic.flutter.model.Message

import com.google.gson.annotations.SerializedName

data class getUserData(
    val username: String,
    @SerializedName("user-id") val userId : String

)
