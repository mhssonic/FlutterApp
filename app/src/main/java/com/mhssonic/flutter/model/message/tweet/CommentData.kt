package com.mhssonic.flutter.model.message.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.mhssonic.flutter.model.message.tweet.TweetData
class CommentData() : TweetData(){
    @JsonProperty("reply-from") var replyFrom : Int? = null
}