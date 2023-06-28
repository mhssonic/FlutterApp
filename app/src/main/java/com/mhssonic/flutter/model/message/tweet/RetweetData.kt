package com.mhssonic.flutter.model.message.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.mhssonic.flutter.model.message.tweet.TweetData



class RetweetData () : TweetData(){
    @JsonProperty() val retweetedMessageId : Int? = null
    @JsonProperty() val retweeterId : Int? = null
}