package com.mhssonic.flutter.ui.userAuth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mhssonic.flutter.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MYTAG" , "HI")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

    }
}