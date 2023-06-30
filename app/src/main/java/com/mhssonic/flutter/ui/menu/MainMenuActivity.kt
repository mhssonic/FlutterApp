package com.mhssonic.flutter.ui.menu

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityLoginBinding
import com.mhssonic.flutter.databinding.ActivityMainBinding
import com.mhssonic.flutter.databinding.ActivityMainMenuBinding


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    private lateinit var timeLineFragment: TimeLineFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var directMessagesFragment: DirectMessagesFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timeLineFragment = TimeLineFragment(getSharedPreferences("cookies", MODE_PRIVATE))
        searchFragment = SearchFragment()
        directMessagesFragment = DirectMessagesFragment()

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(timeLineFragment)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.timeLine -> replaceFragment(timeLineFragment)
                R.id.search -> replaceFragment(searchFragment)
                R.id.directMessages -> replaceFragment(directMessagesFragment)

                else ->{}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}