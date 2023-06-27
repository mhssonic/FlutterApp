package com.mhssonic.flutter.service.http

import android.content.SharedPreferences
import android.content.Context
import android.util.Log
import com.mhssonic.flutter.ui.userAuth.LoginActivity
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

class RetrofitInstance {
    class MyCookieJar(private val sharedPreferences: SharedPreferences) : CookieJar {
        private val cookieStore = ConcurrentHashMap<String, CopyOnWriteArrayList<Cookie>>()

        init {
            // Load cookies from shared preferences
            val cookies = sharedPreferences.getStringSet("cookies", emptySet()) ?: emptySet()
            for (cookieString in cookies) {
                val cookie = Cookie.parse("https://192.168.1.4:5050".toHttpUrl(), cookieString)
                if (cookie != null) {
                    val host = cookie.domain
                    cookieStore[host]?.add(cookie) ?: run {
                        val newList = CopyOnWriteArrayList<Cookie>()
                        newList.add(cookie)
                        cookieStore[host] = newList
                    }
                }
            }
        }

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            for (cookie in cookies) {
                val host = cookie.domain
                cookieStore[host]?.add(cookie) ?: run {
                    val newList = CopyOnWriteArrayList<Cookie>()
                    newList.add(cookie)
                    cookieStore[host] = newList
                }
            }
            //TODO make it more effective

            // Save cookies to shared preferences
            val editor = sharedPreferences.edit()
            val cookieStrings = mutableSetOf<String>()
            for (hostCookies in cookieStore.values) {
                for (cookie in hostCookies) {
                    cookieStrings.add(cookie.toString())
                }
            }
            editor.putStringSet("cookies", cookieStrings)
            editor.apply()
//            Log.v("MYTAG", cookies.toString())
//            cookieStore[url.host] = cookies.toMutableList()
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            Log.v("MYTAG", cookieStore[url.host].toString())
            return cookieStore[url.host] ?: emptyList()
        }
    }

    companion object{
        private const val BASE_URL = "https://192.168.1.4:5050"
        private fun getRetrofitInstance(sharedPreferences: SharedPreferences): Retrofit {
            val cookieJar = MyCookieJar(sharedPreferences)

            val logger = HttpLoggingInterceptor()
            logger.setLevel(HttpLoggingInterceptor.Level.BODY)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(TrustAllCerts()), SecureRandom())

            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .sslSocketFactory(sslSocketFactory, TrustAllCerts())
                .hostnameVerifier { _, _ -> true }
                .cookieJar(cookieJar)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build()
        }

        fun getApiService(sharedPreferences: SharedPreferences): ApiService = getRetrofitInstance(sharedPreferences).create(ApiService::class.java)
    }
}


