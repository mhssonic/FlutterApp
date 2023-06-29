package com.mhssonic.flutter.model.Message.Tweet

import com.fasterxml.jackson.annotation.JsonProperty

class QuoteData: TweetData() {
    var quotedMessageID : Int? = null
}