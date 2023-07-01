package com.mhssonic.flutter.ui.menu.createTweet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityCreateTweetBinding

class CreateTweetActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var binding: ActivityCreateTweetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTweetBinding.inflate(layoutInflater)
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

            imageFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentImage, imageFragment)
                .commit()
        }
    }
}