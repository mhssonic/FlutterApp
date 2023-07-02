package com.mhssonic.flutter.ui.userAuth.Profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityProfileBinding
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.DownloadFileService
import com.mhssonic.flutter.service.http.RetrofitInstance
import com.mhssonic.flutter.ui.userAuth.settings.SettingActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        val user = intent.getSerializableExtra("userProfile") as UserProfileData

        binding.tvName.text = "${user.firstName}  ${user.lastName}"
        binding.tvBio.text = user.biography
        binding.tvJoin.text = "born in ${user.birthdate}"
        binding.tvUsername.text = user.username

        binding.tvFollowerCount.text = user.followerCount.toString()
        binding.tvFollowingCount.text = user.followingCount.toString()

        val sharedPreferencesCookie = getSharedPreferences("cookies", MODE_PRIVATE)
        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)

        val handler = Handler(Looper.getMainLooper())

        val uriProfile : MutableLiveData<Uri> = MutableLiveData()
        DownloadFileService.getFile(serviceApi, user.avatar , compositeDisposable, uriProfile, this)


        uriProfile.observe(this, Observer {
            binding.avatar.setImageURI(uriProfile.value)
        })

        val alreadyFollowed : MutableLiveData<Boolean> = MutableLiveData(false)
        alreadyFollowed.observe(this, Observer{
            if(!it)
                binding.btnFollow.text = "دنبال کردن"
            else
                binding.btnFollow.text = "فرار کردن"
        })

        val uriHeader : MutableLiveData<Uri> = MutableLiveData()
        DownloadFileService.getFile(serviceApi, user.header , compositeDisposable, uriHeader, this)

        uriHeader.observe(this, Observer {
            binding.header.setImageURI(uriHeader.value)
        })

        binding.btnFollow.setOnClickListener{
            it.isEnabled = false
            if(alreadyFollowed.value == true){
                compositeDisposable.add(serviceApi.unfollow(getUserDataByUserId(user.id)).subscribeOn(
                    Schedulers.io()).subscribe({response ->
                    handler.post {
                        it.isEnabled = true
                    }
                    val responseBody = response.string()
                    if(responseBody == "SUCCESS"){
                        alreadyFollowed.postValue(false)
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
            }else{
                compositeDisposable.add(serviceApi.follow(getUserDataByUserId(user.id)).subscribeOn(
                    Schedulers.io()).subscribe({response ->
                    handler.post {
                        it.isEnabled = true
                    }
                    val responseBody = response.string()
                    if(responseBody == "SUCCESS"){
                        alreadyFollowed.postValue(true)
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
        }

        compositeDisposable.add(serviceApi.alreadyFollowed(getUserDataByUserId(user.id)).subscribeOn(
            Schedulers.io()).subscribe({
            val responseBody = it.string()
            if(responseBody == "true"){
                alreadyFollowed.postValue(true)
            }else {
                alreadyFollowed.postValue(false)
            }
        }, {t ->
            Log.v("MYTAG", "failed ${t.message!!}")
        }))



        binding.btnPrev.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}