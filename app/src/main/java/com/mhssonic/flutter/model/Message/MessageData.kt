package com.mhssonic.flutter.model.Message

import com.google.gson.annotations.SerializedName
import okio.IOException
import java.time.LocalDateTime

open class MessageData {
    @SerializedName("messageId") var id: Int? = null
    @SerializedName("author-id") var author: Int? = null
     var text: String? = null
    @SerializedName("attachment-id") var attachment: ArrayList<Int>? = null
    var postingTime: LocalDateTime? = null
}