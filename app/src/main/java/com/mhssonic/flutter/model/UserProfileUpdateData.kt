package com.mhssonic.flutter.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserProfileUpdateData(
    @SerializedName("first-name") var firstName: String = "",
    @SerializedName("last-name") var lastName: String = "",
    @SerializedName("phone-number") var phoneNumber: String= "",
    var email: String= "",
    var country: String= "",
    var birthdate: String= "",
    var biography: String= "",
    var avatar: String = "",
    var header: String ="",
    var username: String= "",
    var password: String= "",
    @SerializedName("confirm-password") var confirmPassword: String = "") : Serializable {
}