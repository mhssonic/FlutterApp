package com.mhssonic.flutter.service.http

import android.content.SharedPreferences
import android.util.Log
import com.fasterxml.jackson.core.JsonParseException
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.mhssonic.flutter.model.Message.MessageData
import com.mhssonic.flutter.model.Message.Tweet.CommentData
import com.mhssonic.flutter.model.Message.Tweet.PollData
import com.mhssonic.flutter.model.Message.Tweet.QuoteData
import com.mhssonic.flutter.model.Message.Tweet.RetweetData
import com.mhssonic.flutter.model.Message.Tweet.TweetData
import com.mhssonic.flutter.model.Message.direct.DirectMessageData
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory


class RetrofitInstance {

//    class PostingTimeDeserializer : JsonDeserializer<LocalDateTime>() {
//        @Throws(IOException::class)
//        override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
//            val codec = parser.codec
//            val node: JsonNode = codec.readTree(parser)
//
//            val year = node.get("year").asInt()
//            val month = node.get("monthValue").asInt()
//            val day = node.get("dayOfMonth").asInt()
//            val hour = node.get("hour").asInt()
//            val minute = node.get("minute").asInt()
//            val second = node.get("second").asInt()
//            val nano = node.get("nano").asInt()
//
//            return LocalDateTime.of(year, month, day, hour, minute, second, nano)
//        }
//    }

    class MessageDataDeserializer : JsonDeserializer<MessageData> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MessageData? {
            val id = json?.asJsonObject?.get("messageId")?.asInt
                ?: throw JsonParseException("ID not found for MessageData")

            return when (id % 6){
                0 -> context?.deserialize(json, TweetData::class.java)
                1 -> context?.deserialize(json, CommentData::class.java)
                2 -> context?.deserialize(json, RetweetData::class.java)
                3 -> context?.deserialize(json, PollData::class.java)
                4 -> context?.deserialize(json, QuoteData::class.java)
                5 -> context?.deserialize(json, DirectMessageData::class.java)
                else -> throw JsonParseException("Unknown MessageData type")
            }
        }

    }


    class MyCookieJar(private val sharedPreferences: SharedPreferences) : CookieJar {
        private val cookieStore = ConcurrentHashMap<String, CopyOnWriteArrayList<Cookie>>()

        init {
            // Load cookies from shared preferences
//            val cookies = sharedPreferences.getStringSet("cookies", emptySet()) ?: emptySet()
//            for (cookieString in cookies) {
//                val cookie = Cookie.parse("https://192.168.1.4:5050".toHttpUrl(), cookieString)
//                if (cookie != null) {
//                    val host = cookie.domain
//                    cookieStore[host]?.add(cookie) ?: run {
//                        val newList = CopyOnWriteArrayList<Cookie>()
//                        newList.add(cookie)
//                        Log.v("CookieList", newList.toString())
//                        cookieStore[host] = newList
//                    }
//                }
//            }
            val tokenCookie : String? = sharedPreferences.getString("token-cookie", null)
            if(tokenCookie != null){
                val cookie = Cookie.parse(RetrofitInstance.BASE_URL.toHttpUrl(), tokenCookie)
                if(cookie != null){
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
            val editor = sharedPreferences.edit()

            for (cookie in cookies) {
                val host = cookie.domain
                cookieStore[host]?.add(cookie) ?: run {
                    val newList = CopyOnWriteArrayList<Cookie>()
                    newList.add(cookie)
                    cookieStore[host] = newList
                }
                if(cookie.name == "token") {
                    editor.putString("token-cookie", cookie.toString())
                }
            }
            //TODO make it more effective

            // Save cookies to shared preferences
//            val cookieStrings = mutableSetOf<String>()
//            for (hostCookies in cookieStore.values) {
//                for (cookie in hostCookies) {
//                    Log.v("Cookie TAG", cookieStrings.toString())
//                    cookieStrings.add(cookie.toString())
//                }
//            }



//            Log.v("Cookie TAG", cookieStrings.toString())
//            editor.putStringSet("cookies", cookieStrings)
            editor.apply()
//            Log.v("MYTAG", cookies.toString())
//            cookieStore[url.host] = cookies.toMutableList()
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
//            Log.v("MYTAG", cookieStore[url.host].toString())
            return cookieStore[url.host] ?: emptyList()
        }
    }

    companion object{
        val BASE_URL = "https://192.168.143.4:5050"
        private fun getRetrofitInstance(sharedPreferences: SharedPreferences): Retrofit {
//            val objectMapper: ObjectMapper = jacksonObjectMapper()
//                .registerModule(SimpleModule()
//                    .addDeserializer(LocalDateTime::class.java, PostingTimeDeserializer())

//                    .addSerializer(LocalDateTime::class.java, MessageData.LocalDateTimeSerializer())
//                    .addDeserializer(LocalDateTime::class.java,
//                        MessageData.LocalDateTimeDeserializer()
//                    )
//                    .addDeserializer(MessageData::class.java, MessageDataDeserializer()))
//                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

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
            val gson = GsonBuilder()
                .registerTypeAdapter(MessageData::class.java, MessageDataDeserializer())
                .create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        }

        fun getApiService(sharedPreferences: SharedPreferences): ApiService = getRetrofitInstance(sharedPreferences).create(ApiService::class.java)
    }
}


