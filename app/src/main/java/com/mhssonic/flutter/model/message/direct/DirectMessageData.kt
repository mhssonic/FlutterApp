package com.mhssonic.flutter.model.message.direct

import com.fasterxml.jackson.annotation.JsonProperty
import com.mhssonic.flutter.model.message.MessageData


class DirectMessageData() : MessageData(){
    @JsonProperty
    var reply : Int? = null

    @JsonProperty("target-id")
    var targetUser : Int? = null
}