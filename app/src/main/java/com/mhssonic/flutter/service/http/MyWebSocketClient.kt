import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.mhssonic.flutter.model.FriendDirectMessages
import com.mhssonic.flutter.model.Message.MessageData
import com.mhssonic.flutter.model.Message.direct.DirectMessageData
import com.mhssonic.flutter.service.http.RetrofitInstance
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.handshake.ServerHandshakeBuilder
import java.lang.reflect.Type
import java.net.URI
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalDateTimeDeserializerForGolang : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        val dateString = json?.asString
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return LocalDateTime.parse(dateString, formatter)
    }
}

class MyWebSocketClient(
    private val serverUri: URI,
    private val friendId: Int,
    private val messageListener: (FriendDirectMessages) -> Unit,
) : WebSocketClient(serverUri) {
    private val cookieJar = RetrofitInstance.getCookieJar()

    private val gson = GsonBuilder()
        .registerTypeAdapter(
            LocalDateTime::class.java,
            LocalDateTimeDeserializerForGolang()
        )
        .create()

    init {
        val cookies = cookieJar.loadForRequest(RetrofitInstance.BASE_URL.toHttpUrl())
        if (cookies.isNotEmpty()) {
            val cookieHeader = buildCookieHeader(cookies)
            addHeader("Cookie", cookieHeader)
        }
        addHeader("target-id", friendId.toString())
    }

    override fun onOpen(handshakedata: ServerHandshake) {
        // Called when the connection is established
        Log.v("MyWebSocket", "WebSocket connection opened")
    }


    override fun onClose(code: Int, reason: String, remote: Boolean) {
        // Called when the connection is closed
        Log.v("MyWebSocket", "WebSocket connection closed")
    }

    override fun onMessage(message: String) {
        // Called when a message is received
        Log.v("MyWebSocket", "Received message: $message")

        // Deserialize the received JSON response using Gson
        val response = gson.fromJson(message, FriendDirectMessages::class.java)

        messageListener.invoke(response)
    }

    override fun onError(ex: Exception) {
        // Called when an error occurs
        Log.v("MyWebSocket", "Error: ${ex.message}")
    }


    fun send(directMessage: DirectMessageData){
        super.send(gson.toJson(directMessage))
    }

    private fun buildCookieHeader(cookies: List<Cookie>): String {
        val cookieList = ArrayList<String>()
        for (cookie in cookies) {
            cookieList.add(cookie.name + "=" + cookie.value)
        }
        return cookieList.joinToString("; ")
    }
}
