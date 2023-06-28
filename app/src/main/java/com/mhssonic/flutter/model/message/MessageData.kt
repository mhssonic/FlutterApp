package com.mhssonic.flutter.model.message

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

open class MessageData() {
    @JsonProperty("messageId") open var id: Int? = null
    @JsonProperty open var author: Int? = null
    @JsonProperty open var text: String? = null
    @JsonProperty open var attachment: Set<Int>? = null
    @JsonProperty open var postingTime: LocalDateTime? = null

//    @JsonProperty
//    fun setPostingTime(postingTime: LocalDateTime) {
//        // Setter method implementation
//        this.postingTime = postingTime
//    }
}