package com.mhssonic.flutter.service.http

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
        private fun downloadFile(responseBody: ResponseBody?, fileName:String): Uri? {
            try {
                if(responseBody == null)
                    return null
                else{
                    val fileDir = File(Environment.getExternalStorageDirectory(), "flutter")
                    if (!fileDir.exists()) {
                        fileDir.mkdirs()
                    }
                    val outputFile = File(fileDir, fileName)

                    // Delete any existing file with the same name
                    if (outputFile.exists()) {
                        outputFile.delete()
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
                e.printStackTrace()
            }

            return null
        }

        public fun getFile(serviceApi:ApiService ,attachment: Int?, compositeDisposable: CompositeDisposable, uri: MutableLiveData<Uri>){
            compositeDisposable.add(serviceApi.downloadFile(AttachmentIdData(attachment))
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    val format = response.headers()["Content-Type"]?.format("file/%s")?.split("/")?.get(1)
                    val downloadedFileUri = downloadFile(response.body(), "$attachment.$format")
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