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


class SignUpThird : SignUp() {
    private lateinit var btnNextFragment: Button
    private lateinit var btnPreFragment: Button
    private lateinit var edEmail: EditText
    private lateinit var edPhonenumber: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_third, container, false)

        btnNextFragment = view.findViewById(R.id.btNext)
        btnPreFragment = view.findViewById(R.id.brPrev)
        edEmail = view.findViewById(R.id.etEmailAddress)
        edPhonenumber = view.findViewById(R.id.etPhone)


        btnNextFragment.setOnClickListener {

            val fourthFragment = SignUpFourth()

            val phonenumber = edPhonenumber.text.toString()
            val email = edEmail.text.toString()

            val firstName = arguments?.getString("first_name")
            val lastName = arguments?.getString("last_name")


            if (phonenumber.isEmpty() && email.isEmpty()) {
                emptyToast()
            } else {
                val bundle = Bundle()
                bundle.putString("first_name", firstName)
                bundle.putString("last_name", lastName)

                fourthFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fourthFragment)
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