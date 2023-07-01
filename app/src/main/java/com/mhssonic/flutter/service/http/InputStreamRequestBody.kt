package com.mhssonic.flutter.service.http

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.buffer
import okio.source
import java.io.InputStream


class InputStreamRequestBody(
    private val inputStream: InputStream,
    private val contentType: MediaType
) : RequestBody() {

    override fun contentType(): MediaType? {
        return contentType
    }

    override fun contentLength(): Long {
        return -1 // or provide the actual content length if known
    }

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE.toInt())
        var bytesRead: Int
        val source = inputStream.source().buffer()

        while (source.read(buffer).also { bytesRead = it } != -1) {
            sink.write(buffer, 0, bytesRead)
        }
        source.close()
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048L
    }
}