package com.mhssonic.flutter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mhssonic.flutter.databinding.ActivityMainBinding
import com.mhssonic.flutter.ui.menu.createTweet.CreateTweetActivity
import com.mhssonic.flutter.ui.menu.MainMenuActivity
import com.mhssonic.flutter.ui.menu.TimeLineFragment
import com.mhssonic.flutter.ui.userAuth.login.LoginActivity
import com.mhssonic.flutter.ui.userAuth.settings.SettingActivity
import com.mhssonic.flutter.ui.userAuth.sign_up.SignUpActivity

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val intent = Intent(this, SettingActivity::class.java)
        val intent = Intent(this, MainMenuActivity::class.java)
//        val intent = Intent(this, ::class.java)
        startActivity(intent)
    }
}