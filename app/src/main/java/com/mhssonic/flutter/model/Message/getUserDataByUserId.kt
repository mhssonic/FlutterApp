package com.mhssonic.flutter.model.Message

import com.google.gson.annotations.SerializedName

data class getUserDataByUserId(
    @SerializedName("user-id") val userId : String
)
