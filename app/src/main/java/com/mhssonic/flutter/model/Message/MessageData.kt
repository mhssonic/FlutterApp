package com.mhssonic.flutter.model.Message

import com.google.gson.annotations.SerializedName
import okio.IOException
import java.time.LocalDateTime

open class MessageData {
    @SerializedName("messageId") var id: Int? = null
    @SerializedName("author-id") var author: Int? = null
     var text: String? = null
//    @SerializedName("attachment-id") var attachment: ArrayList<Int>? = null

//    @SerializedName("postingTime")
//    @JsonSerialize(using = LocalDateTimeSerializer::class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
//    var postingTime: LocalDateTime? = null


//    class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
//        @Throws(IOException::class, JsonProcessingException::class)
//        override fun serialize(
//            localDateTime: LocalDateTime,
//            jsonGenerator: JsonGenerator,
//            serializerProvider: SerializerProvider
//        ) {
//            jsonGenerator.writeString(localDateTime.toString())
//        }
//    }
//
//    class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
//        @Throws(IOException::class, JsonProcessingException::class)
//        override fun deserialize(
//            jsonParser: JsonParser,
//            deserializationContext: DeserializationContext
//        ): LocalDateTime {
//            val node: JsonNode = jsonParser.codec.readTree(jsonParser)
//            val dateTimeString = node.asText()
//            return LocalDateTime.parse(dateTimeString)
//        }
//    }
}