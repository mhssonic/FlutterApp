package com.mhssonic.flutter.ui.menu.createTweet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityCreateTweetBinding
import com.mhssonic.flutter.model.Message.create_message.TweetCreateData
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.InputStream

class CreateTweetActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivityCreateTweetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTweetBinding.inflate(layoutInflater)

        val sharedPreferencesCookie = getSharedPreferences("cookies", MODE_PRIVATE)
        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)
        val handler = Handler(Looper.getMainLooper())

        binding.btnTweet.setOnClickListener{button ->
            button.isEnabled = false
            val hashtag = binding.edHashtag.text.toString()
            val tweetCreateData = TweetCreateData(binding.edTweet.text.toString(), ArrayList(hashtag.split('\n')))

            compositeDisposable.add(serviceApi.tweet(tweetCreateData).subscribeOn(
                Schedulers.io()).subscribe({
                val responseBody = it.string()
                if(responseBody == "SUCCESS"){
                    handler.post {
                        button.isEnabled = true
                        finish()
                    }
                }else {
                    handler.post {
                        button.isEnabled = true
                        Toast.makeText(applicationContext, "Your username or password is wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }, {t ->
                Log.v("MYTAG", "failed ${t.message!!}")
            }))


        }
        setContentView(binding.root)
        }

    fun openImagePicker(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data

            val imageFragment = ImageFragment()
            Log.i("MYTAG" , "$selectedImageUri")
            val bundle = Bundle().apply {
                putParcelable("imageUri", selectedImageUri)
            }
            Log.i("MYTAG" , "$selectedImageUri")

            val inputStream: InputStream? = selectedImageUri?.let {
                contentResolver.openInputStream(
                    it
                )
            }
            val bufferedInputStream = BufferedInputStream(inputStream)
            val bufferSize = 1024 // or any other suitable buffer size
            val buffer = ByteArray(bufferSize)
            var bytesRead: Int
            var totalBytes = 0
            while (bufferedInputStream.read(buffer).also { bytesRead = it } != -1) {
                totalBytes += bytesRead
            }

            val imageSizeBytes = totalBytes





            imageFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentImage, imageFragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}