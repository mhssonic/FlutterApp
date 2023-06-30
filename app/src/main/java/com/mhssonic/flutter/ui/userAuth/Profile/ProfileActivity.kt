package com.mhssonic.flutter.ui.userAuth.Profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityProfileBinding
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val compositeDisposable = CompositeDisposable()
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

        val sharedPreferencesCookie = getSharedPreferences("cookies", MODE_PRIVATE)
        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)

        val handler = Handler(Looper.getMainLooper())

        binding.btnFollow.setOnClickListener{
            it.isEnabled = false
            compositeDisposable.add(serviceApi.follow(getUserDataByUserId(user.id)).subscribeOn(
                Schedulers.io()).subscribe({response ->
                handler.post {
                    it.isEnabled = true
                }
                val responseBody = response.string()
                if(responseBody == "SUCCESS"){
                    handler.post {
                        Toast.makeText(applicationContext, "hola", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    handler.post {
                        Toast.makeText(applicationContext,responseBody, Toast.LENGTH_SHORT).show()
                    }
                }
            }, {t ->
                handler.post {
                    it.isEnabled = true
                    Log.v("MYTAG", "failed ${t.message!!}")
                }
            }))
        }

        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}