package com.mhssonic.flutter.model.Message.direct

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import com.mhssonic.flutter.model.Message.MessageData


class DirectMessageData() : MessageData(){
    var reply : Int? = null

    @SerializedName("target-id")
    var targetUser : Int? = null
}