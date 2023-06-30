package com.mhssonic.flutter.ui.userAuth.Profile

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityProfileBinding
import com.mhssonic.flutter.model.UserProfileData

class ProfileActivity() : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        val user = intent.getSerializableExtra("user") as UserProfileData


        binding.tvName.text = "${user.firstName}  ${user.lastName}"
        binding.tvBio.text = user.biography
        binding.tvJoin.text = "he/she been in this world since ${user.birthdate}"
        binding.tvUsername.text = user.username

        binding.tvFollowerCount.text = user.followerCount.toString()
        binding.tvFollowingCount.text = user.followingCount.toString()

        setContentView(binding.root)
    }

}