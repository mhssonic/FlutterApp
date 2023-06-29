package com.mhssonic.flutter.model.Message

import com.fasterxml.jackson.annotation.JsonProperty

class Choice {
    var choiceId : Int? = null
    var text: String? = null
    var voters: ArrayList<Int>? = null
}