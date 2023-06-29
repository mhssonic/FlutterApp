package com.mhssonic.flutter.model.Message.Tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

class PollData : TweetData() {
    var choiceId: ArrayList<Int>? = null
    var choices: ArrayList<Any>? = null
}