package com.mhssonic.flutter.ui.userAuth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mhssonic.flutter.R

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

        btnNextFragment.setOnClickListener {

            val thirdFragment = SignUpThird()
            val username = edUsername.text.toString()
            val password = edPassword.text.toString()
            val conPassword = edConPassword.text.toString()
            val firstName = arguments?.getString("first_name")
            val lastName = arguments?.getString("last_name")

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
                bundle.putString("first_name", firstName)
                bundle.putString("last_name", lastName)
                bundle.putString("username", username)
                bundle.putString("password", password)

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