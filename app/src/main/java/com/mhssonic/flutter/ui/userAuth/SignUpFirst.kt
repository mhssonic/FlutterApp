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


class SignUpFirst : SignUp() {
    private lateinit var btnChangeFragment: Button
    private lateinit var edFirstName: EditText
    private lateinit var edLastName: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_first, container, false)


        edFirstName = view.findViewById(R.id.ptFirstName)
        edLastName = view.findViewById(R.id.ptLastName)
        btnChangeFragment = view.findViewById(R.id.btNext)

        btnChangeFragment.setOnClickListener {

            val secondFragment = SignUpSecond()
            val firstName = edFirstName.text.toString()
            val lastName = edLastName.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty()) {
                emptyToast()
            } else {
                val bundle = Bundle()
                secondFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, secondFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        return view
    }
}