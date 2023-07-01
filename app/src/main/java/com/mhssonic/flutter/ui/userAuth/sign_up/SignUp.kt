package com.mhssonic.flutter.ui.userAuth.sign_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.UserSignUpData

//TODO signUpForm....

open class SignUp : Fragment() {
    open val user = UserSignUpData()

    fun emptyToast(){
        Toast.makeText(
            requireContext(),
            "لطفا اطلاعات مورد نظر را وارد کنید",
            Toast.LENGTH_SHORT
        ).show()
    }
    fun emptyToast(text : String?){
        var tempText = text ?: ""
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
   }
}