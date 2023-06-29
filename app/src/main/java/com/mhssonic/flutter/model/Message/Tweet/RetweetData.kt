package com.mhssonic.flutter.model.Message.Tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName


class RetweetData : TweetData(){
    val retweetedMessageId : Int? = null
    val retweeterId : Int? = null
}