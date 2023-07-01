package com.mhssonic.flutter.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserProfileData : Serializable{
    var id: Int? = null
    @SerializedName("first-name") var firstName: String = ""
    @SerializedName("last-name") var lastName: String = ""
    var country: String= ""
    var birthdate: String= ""
    var biography: String= ""
    var avatar: Int? = null
    var header: Int?= null
    var username: String= ""
    @SerializedName("count-follower") var followerCount: Int = 0
    @SerializedName("count-following") var followingCount: Int = 0

}