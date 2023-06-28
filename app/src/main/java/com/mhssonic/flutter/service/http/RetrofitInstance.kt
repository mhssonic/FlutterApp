package com.mhssonic.flutter.service.http

import android.content.SharedPreferences
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mhssonic.flutter.model.message.MessageData
import com.mhssonic.flutter.model.message.direct.DirectMessageData
import com.mhssonic.flutter.model.message.tweet.CommentData
import com.mhssonic.flutter.model.message.tweet.PollData
import com.mhssonic.flutter.model.message.tweet.QuoteData
import com.mhssonic.flutter.model.message.tweet.RetweetData
import com.mhssonic.flutter.model.message.tweet.TweetData
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

class RetrofitInstance {

    class PostingTimeDeserializer : JsonDeserializer<LocalDateTime>() {
        @Throws(IOException::class)
        override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
            val codec = parser.codec
            val node: JsonNode = codec.readTree(parser)

            val year = node.get("year").asInt()
            val month = node.get("monthValue").asInt()
            val day = node.get("dayOfMonth").asInt()
            val hour = node.get("hour").asInt()
            val minute = node.get("minute").asInt()
            val second = node.get("second").asInt()
            val nano = node.get("nano").asInt()

            return LocalDateTime.of(year, month, day, hour, minute, second, nano)
        }
    }

    class MessageDataDeserializer : JsonDeserializer<MessageData>() {
        override fun deserialize(
            parser: JsonParser,
            context: DeserializationContext
        ): MessageData {
            val node: JsonNode = parser.codec.readTree(parser)
            val idNode = node.get("messageId")

            if (idNode == null || idNode.isNull) {
                throw JsonParseException(parser, "ID not found for MessageData")
            }

            val id = idNode.asInt()
            return when (id % 6){
                0 -> context.readValue(node.traverse(), TweetData::class.java)
                1 -> context.readValue(node.traverse(), CommentData::class.java)
                2 -> context.readValue(node.traverse(), RetweetData::class.java)
                3 -> context.readValue(node.traverse(), PollData::class.java)
                4 -> context.readValue(node.traverse(), QuoteData::class.java)
                5 -> context.readValue(node.traverse(), DirectMessageData::class.java)
                else -> throw JsonParseException(parser, "Unknown MessageData type")
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
                val cookie = Cookie.parse("https://192.168.1.4:5050".toHttpUrl(), tokenCookie)
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
        private const val BASE_URL = "https://192.168.1.4:5050"
        private fun getRetrofitInstance(sharedPreferences: SharedPreferences): Retrofit {
            val objectMapper: ObjectMapper = jacksonObjectMapper()
                .registerModule(SimpleModule().addDeserializer(LocalDateTime::class.java, PostingTimeDeserializer()))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

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
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build()
        }

        fun getApiService(sharedPreferences: SharedPreferences): ApiService = getRetrofitInstance(sharedPreferences).create(ApiService::class.java)
    }
}


