package com.mhssonic.flutter.service.http

import com.mhssonic.flutter.model.Message.UsersProfileData
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.model.Message.getUserDataByUsername
import com.mhssonic.flutter.model.MessageIdData
import com.mhssonic.flutter.model.TimeLineData
import com.mhssonic.flutter.model.UserLoginData
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.model.UserSignUpData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
//    @Headers("Content-Type : application/json")
    @POST("/sign-in")
    fun login(@Body data: UserLoginData): Call<ResponseBody>

    @POST("/sign-up")
    fun signUp(@Body data: UserSignUpData): Call<ResponseBody>

    @POST("/show-profile")
    fun getProfileUser(@Body data: getUserDataByUserId): Observable<UserProfileData>

    @POST("/search-users")
    fun searchUsersProfile(@Body data: getUserDataByUsername): Observable<UsersProfileData>

    @POST("/show-timeline")
    @Headers("Content-Type: application/json")
    fun getTimeLine(): Observable<TimeLineData>

    @POST("/follow")
    fun follow(@Body data: getUserDataByUserId): Observable<ResponseBody>

    @POST("/like")
    fun like(@Body data: MessageIdData): Call<ResponseBody>
}