package com.mhssonic.flutter.ui.userAuth.sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.UserSignUpData

class SignUpSecond : SignUp() {
    private lateinit var btnNextFragment: Button
    private lateinit var btnPreFragment: Button

    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var edConPassword: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_second, container, false)

        edUsername = view.findViewById(R.id.ptUserName)
        edPassword = view.findViewById(R.id.ptPassword)
        edConPassword = view.findViewById(R.id.ptConfirmPass)
        btnNextFragment = view.findViewById(R.id.btNext)
        btnPreFragment = view.findViewById(R.id.brPrev)


        edPassword.apply {
            gravity = Gravity.END

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    gravity = if (s.isNullOrEmpty())  Gravity.END else Gravity.START
                }
            })
        }

        edConPassword.apply {
            gravity = Gravity.END

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    gravity = if (s.isNullOrEmpty())  Gravity.END else Gravity.START
                }
            })
        }

        btnNextFragment.setOnClickListener {

            val thirdFragment = SignUpThird()
            val username = edUsername.text.toString()
            val password = edPassword.text.toString()
            val conPassword = edConPassword.text.toString()

            if (password != conPassword){
                emptyToast()
            }
            else if (username.isEmpty() || password.isEmpty() || conPassword.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "لطفا اطلاعات مورد نظر را وارد کنید",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val bundle = Bundle()

                val user = arguments?.getSerializable("user") as? UserSignUpData
                if (user != null) {
                    user.username = username
                    user.password = password
                    Log.i("MYTAG", "${user.toString()}")

                }

                bundle.putSerializable("user", user)
                thirdFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, thirdFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        btnPreFragment.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}