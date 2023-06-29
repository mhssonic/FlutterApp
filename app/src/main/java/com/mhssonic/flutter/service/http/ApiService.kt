package com.mhssonic.flutter.service.http

import com.mhssonic.flutter.model.TimeLineData
import com.mhssonic.flutter.model.UserLoginData
import com.mhssonic.flutter.model.UserSignUpData
import io.reactivex.Observable
import io.reactivex.Observer
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
//    @Headers("Content-Type : application/json")
    @POST("/sign-in")
    fun login(@Body data: UserLoginData): Call<ResponseBody>

    @POST("/sign-in")
    fun signUp(@Body data: UserSignUpData): Call<ResponseBody>

    @POST("/show-timeline")
    @Headers("Content-Type: application/json")
    fun getTimeLine(): Observable<TimeLineData>
}