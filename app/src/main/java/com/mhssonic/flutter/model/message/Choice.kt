package com.mhssonic.flutter.model.message

import com.fasterxml.jackson.annotation.JsonProperty

class Choice {
    @JsonProperty() var choiceId : Int? = null
    @JsonProperty() var text: String? = null
    @JsonProperty() var voters: ArrayList<Int>? = null
}