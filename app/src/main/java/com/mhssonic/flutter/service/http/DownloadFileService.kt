package com.mhssonic.flutter.service.http

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mhssonic.flutter.model.AttachmentIdData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

class DownloadFileService {
    companion object{
        private fun downloadFile(responseBody: ResponseBody?, fileName:String, context: Context): Uri? {
            try {
                if(responseBody == null)
                    return null
                else{
                    val fileDir = File(context.getExternalFilesDir(null), "flutter")
                    if (!fileDir.exists()) {
                        Log.v("dododo", fileDir.mkdirs().toString())
                    }
                    val outputFile = File(fileDir, fileName)

                    // Delete any existing file with the same name
                    if (outputFile.exists()) {
                        return Uri.fromFile(outputFile)
                    }

                    val inputStream = responseBody.byteStream()
                    val outputStream = FileOutputStream(outputFile)

                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }

                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()

                    // Return the URI of the downloaded file
                    return Uri.fromFile(outputFile)
                }
            } catch (e: Exception) {
                Log.v("FileSAViNG", e.message!!)
            }

            return null
        }

        public fun getFile(serviceApi:ApiService ,attachment: Int?, compositeDisposable: CompositeDisposable, uri: MutableLiveData<Uri>, context: Context){
            compositeDisposable.add(serviceApi.downloadFile(AttachmentIdData(attachment))
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    val format = response.headers()["Content-Type"]?.format("file/%s")?.split("/")?.get(1)
                    val downloadedFileUri = downloadFile(response.body(), "$attachment.$format", context)
                    if (downloadedFileUri != null) {
                        // Handle the downloaded file URI
                        uri.postValue(downloadedFileUri)
                    } else {
                        // Handle download error
                        Log.v("FILETAG", "File download failed")
                    }
                }, { t ->
                    // Handle network or other errors
                    Log.v("FILETAG", "Request failed: ${t.message}")
                }))
        }
    }
}