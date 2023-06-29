package com.mhssonic.flutter.model

import java.io.Serializable

class UserSignUpData : Serializable {
    var firstName: String = ""
    var lastName: String = ""
    var phoneNumber: String= ""
    var email: String= ""
    var country: String= ""
    var birthdate: String= ""
    var biography: String= ""
    var avatar: String= ""
    var header: String= ""
    var username: String= ""
    var password: String= ""

    override fun toString(): String {
        return " $firstName , $lastName , $phoneNumber ,$email , $username , $password , $country "
    }
}