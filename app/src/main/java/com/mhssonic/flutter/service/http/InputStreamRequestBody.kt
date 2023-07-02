package com.mhssonic.flutter.service.http

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.buffer
import okio.source
import java.io.InputStream
import java.lang.Integer.max
import java.lang.Integer.min


class InputStreamRequestBody(
    private val inputStream: InputStream,
    private val contentType: MediaType
) : RequestBody() {

    override fun contentType(): MediaType? {
        return contentType
    }

    override fun contentLength(): Long {
        val source = inputStream.source().buffer()
        val readByteArray = source.readByteArray()
        return readByteArray.size.toLong()
    }

    override fun writeTo(sink: BufferedSink) {
        val source = inputStream.source().buffer()
        val readByteArray = source.readByteArray()
        sink.write(readByteArray)
        sink.flush()
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048L
    }
}