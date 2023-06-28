package com.mhssonic.flutter.model.message.tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.mhssonic.flutter.model.message.tweet.TweetData
class PollData() : TweetData() {
    @JsonProperty() var choiceId: ArrayList<Int>? = null
    @JsonProperty() var choices: ArrayList<Any>? = null
}