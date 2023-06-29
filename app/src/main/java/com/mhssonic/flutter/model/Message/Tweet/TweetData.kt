package com.mhssonic.flutter.model.Message.Tweet

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import com.mhssonic.flutter.model.Message.MessageData

open class TweetData : MessageData() {
    @SerializedName("retweet-count") var retweet: Int? = null
    var favestar: Boolean? = null
     var likes: Int? = null
//  var comment: ArrayList<Int>? = null
//   var hashtag: ArrayList<String>? = null
}
