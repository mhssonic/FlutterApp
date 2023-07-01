package com.mhssonic.flutter.model.Message.create_message

import com.google.gson.annotations.SerializedName

data class TweetCreateData(
    var text: String = "",
    var hashtag : ArrayList<String>? = null,
    @SerializedName("attachment-id") var attachmentId : ArrayList<Int>? = null
)