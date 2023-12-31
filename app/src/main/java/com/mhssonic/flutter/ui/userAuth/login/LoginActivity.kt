package com.mhssonic.flutter.ui.userAuth.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mhssonic.flutter.databinding.ActivityLoginBinding
import com.mhssonic.flutter.model.UserLoginData
import com.mhssonic.flutter.service.http.RetrofitInstance
import com.mhssonic.flutter.ui.menu.MainMenuActivity
import com.mhssonic.flutter.ui.userAuth.sign_up.SignUpActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val TAG = "babaE"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreference : SharedPreferences
    private lateinit var sharedEditor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO make it clean and add if he has already logged in
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = getSharedPreferences("cookies", MODE_PRIVATE)

        val intentSignUp = Intent(this, SignUpActivity::class.java)
        val intentMainMenu = Intent(this, MainMenuActivity::class.java)
        val serviceApi = RetrofitInstance.getApiService(sharedPreference)

        if (sharedPreference.getString("token-cookie", null) != null){
            finish()
            startActivity(intentMainMenu)
        }

        binding.editTextPassword.apply {
            gravity = Gravity.END

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    gravity = if (s.isNullOrEmpty())  Gravity.END else Gravity.START
                }
            })
        }

        binding.loginButton.setOnClickListener {
            binding.loginButton.isEnabled = false
            val data = UserLoginData(binding.editTextUsername.text.toString()
                , binding.editTextPassword.text.toString())

            val call = serviceApi.login(data)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        finish()
                        startActivity(intentMainMenu)
                    }else{
                        Toast.makeText(applicationContext, "Your username or password is wrong", Toast.LENGTH_LONG).show()
                    }
                    binding.loginButton.isEnabled = true
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    binding.loginButton.isEnabled = true
                    //TODO change it to there is a problem with your network
                    Toast.makeText(applicationContext, "An error occurred: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.signUpButton.setOnClickListener {
            startActivity(intentSignUp)
        }
    }
}