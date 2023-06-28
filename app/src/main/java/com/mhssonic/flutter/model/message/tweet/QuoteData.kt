package com.mhssonic.flutter.model.message.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.mhssonic.flutter.model.message.tweet.TweetData
class QuoteData(): TweetData() {
    @JsonProperty() var quotedMessageID : Int? = null
}