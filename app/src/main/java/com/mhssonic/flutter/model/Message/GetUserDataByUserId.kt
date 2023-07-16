package com.mhssonic.flutter.model.Message

import com.google.gson.annotations.SerializedName

data class GetUserDataByUserId(
    @SerializedName("user-id") val userId : Int?
)
