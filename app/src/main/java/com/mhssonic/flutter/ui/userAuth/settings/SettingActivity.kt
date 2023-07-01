package com.mhssonic.flutter.ui.userAuth.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivitySettingBinding
import com.mhssonic.flutter.model.UserSignUpData

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)

        val user = UserSignUpData()
        user.avatar = binding.btnAvatar
        user.header = binding.btnHeader

        setContentView(binding.root)
    }



}