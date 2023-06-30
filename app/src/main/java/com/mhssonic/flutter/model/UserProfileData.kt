package com.mhssonic.flutter.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserProfileData{
    @SerializedName("first-name") var firstName: String = ""
    @SerializedName("last-name") var lastName: String = ""
    var country: String= ""
    var birthdate: String= ""
    var biography: String= ""
    var avatar: String= ""
    var header: String= ""
    var username: String= ""
}