package com.mhssonic.flutter.ui.menu

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityLoginBinding
import com.mhssonic.flutter.databinding.ActivityMainBinding
import com.mhssonic.flutter.databinding.ActivityMainMenuBinding


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    private val timeLineFragment = TimeLineFragment()
    private val searchFragment = SearchFragment()
    private val directMessagesFragment = DirectMessagesFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(timeLineFragment)

        binding.bottomNavigation.setOnItemReselectedListener {
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