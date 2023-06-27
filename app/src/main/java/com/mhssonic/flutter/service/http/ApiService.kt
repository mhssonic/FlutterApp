package com.mhssonic.flutter.service.http

import com.mhssonic.flutter.model.UserLoginData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
//    @Headers("Content-Type : application/json")
    @POST("/sign-in")
    fun login(@Body data: UserLoginData): Call<ResponseBody>
}