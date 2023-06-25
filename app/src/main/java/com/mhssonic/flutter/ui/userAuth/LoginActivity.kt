package com.mhssonic.flutter.ui.userAuth

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mhssonic.flutter.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreference : SharedPreferences
    private lateinit var sharedEditor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreference = getSharedPreferences("user_information", MODE_PRIVATE)
        sharedEditor = sharedPreference.edit()

        //TODO change color of buttons
        binding.editTextPassword.apply {
            gravity = Gravity.END

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    gravity = if (s.isNullOrEmpty()) {
                        // If the edit text is empty, set gravity to right (end)
                        Gravity.END
                    } else {
                        // If the edit text has text, set gravity to left (start)
                        Gravity.START
                    }
                }
            })

            binding.loginButton.setOnClickListener {
                sharedEditor.apply{
                    putString("token", binding.editTextPassword.text.toString())
                    commit()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(applicationContext, sharedPreference.getString("token", ""), Toast.LENGTH_SHORT).show()
    }
}