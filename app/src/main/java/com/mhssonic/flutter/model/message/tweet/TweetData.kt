package com.mhssonic.flutter.model.message.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.mhssonic.flutter.model.message.MessageData

open class TweetData() : MessageData(){
    @JsonProperty("retweet-count") open var retweet: Int? = null
    @JsonProperty open var favestar : Boolean? = null
    @JsonProperty open var like: Set<Int>? = null
    @JsonProperty open var likes: Int? = null
    @JsonProperty open var comment : Set<Int>? = null
    @JsonProperty open var hashtag : ArrayList<String>? = null
}
