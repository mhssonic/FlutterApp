package com.mhssonic.flutter.tweet.retweet

import android.os.Bundle
import android.os.PersistableBundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityCommentBinding
import com.mhssonic.flutter.databinding.ActivityRetweetBinding

class RetweetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetweetBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityRetweetBinding.inflate(layoutInflater)



        setContentView(binding.root)
    }
}