package com.mhssonic.flutter.model.Message.Tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

class CommentData() : TweetData(){
    @SerializedName("reply-from") var replyFrom : Int? = null
}