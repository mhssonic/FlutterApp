package com.mhssonic.flutter.ui.userAuth.settings

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View.VISIBLE
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivitySettingBinding
import com.mhssonic.flutter.model.UserUri

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val PICK_IMAGE_REQUEST = 1
    private var flag = 1
    val bundle = Bundle()
    private val userUri = UserUri()



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button3.setOnClickListener {
            flag = 1
            openGallery()
        }
        binding.button6.setOnClickListener {
            flag = 2
            openGallery()
        }


        val secondFragment = SettingFirst()
        bundle.putSerializable("userUri", userUri)
        secondFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, secondFragment)
            .commit()



    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val drawable = BitmapDrawable(resources, bitmap)
            if (flag == 1) {
                binding.linearLayout2.background = drawable
                if (imageUri != null) {
                    userUri.headerUri = imageUri
                }
            }
            else {

                binding.button6.text = "+"
                binding.imageView9.setBackgroundDrawable(drawable)
                binding.imageView9.visibility = VISIBLE
                if (imageUri != null) {
                    userUri.avatarUri = imageUri
                }
            }
        }
    }
}

