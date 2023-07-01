package com.mhssonic.flutter.service.http

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.InputStream

class UploadFileService {
    companion object{
        fun uploadFile(fileUri: Uri?, contentResolver: ContentResolver, serviceApi: ApiService, attachmentMutable: MutableLiveData<Int>, compositeDisposable: CompositeDisposable){
            if(fileUri == null)
                return
            val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
            val format = contentResolver.getType(fileUri)
            val contentType = "file/$format".toMediaTypeOrNull()

            if (inputStream != null && contentType != null) {
                val requestBody = InputStreamRequestBody(inputStream, contentType)
                compositeDisposable.add(serviceApi.uploadFile(requestBody).subscribeOn(Schedulers.io())
                    .subscribe({ response ->
                        val bodyString =  response.string()
                        attachmentMutable.postValue(bodyString.toInt())
                    }, { t ->
                        Log.v("FILETAG", "Request failed: ${t.message}")
                    }))
            }
        }
    }
}