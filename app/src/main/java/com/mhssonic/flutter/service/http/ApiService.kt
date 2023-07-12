package com.mhssonic.flutter.service.http

import com.mhssonic.flutter.model.AttachmentIdData
import com.mhssonic.flutter.model.CommentsData
import com.mhssonic.flutter.model.FriendDirectMessages
import com.mhssonic.flutter.model.Message.UsersProfileData
import com.mhssonic.flutter.model.Message.create_message.TweetCreateData
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.model.Message.getUserDataByUsername
import com.mhssonic.flutter.model.MessageIdData
import com.mhssonic.flutter.model.TimeLineData
import com.mhssonic.flutter.model.UserLoginData
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.model.UserProfileUpdateData
import com.mhssonic.flutter.model.UserSignUpData
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

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

    @POST("/get-friends")
    @Headers("Content-Type: application/json")
    fun getFriends(): Observable<UsersProfileData>

    @POST("/show-timeline")
    @Headers("Content-Type: application/json")
    fun getTimeLine(): Observable<TimeLineData>

    @POST("/show-direct")
    fun getDirectMessageHttp(@Body data: getUserDataByUserId): Observable<FriendDirectMessages>

    @POST("/show-tweet")
    fun getComments(@Body data: MessageIdData): Observable<CommentsData>

    @POST("/follow")
    fun follow(@Body data: getUserDataByUserId): Observable<ResponseBody>

    @POST("/already-follow")
    fun alreadyFollowed(@Body data: getUserDataByUserId): Observable<ResponseBody>

    @POST("/unfollow")
    fun unfollow(@Body data: getUserDataByUserId): Observable<ResponseBody>


    @POST("/like")
    fun like(@Body data: MessageIdData): Observable<ResponseBody>

    @POST("/already-liked")
    fun alreadyLiked(@Body data: MessageIdData): Observable<ResponseBody>

    @POST("/unlike")
    fun unlike(@Body data: MessageIdData): Observable<ResponseBody>

    @POST("/download-file")
    @Streaming
    fun downloadFile(@Body data: AttachmentIdData): Observable<Response<ResponseBody>>

    @POST("/upload-file")
    @Streaming
    fun uploadFile(@Body requestBody: RequestBody): Observable<ResponseBody>

    @POST("/update-profile")
    fun updateProfile(@Body data: UserProfileUpdateData): Observable<ResponseBody>

    @POST("/tweet")
    fun tweet(@Body data: TweetCreateData): Observable<ResponseBody>

    @POST("/retweet")
    fun retweet(@Body data: MessageIdData): Observable<ResponseBody>
}