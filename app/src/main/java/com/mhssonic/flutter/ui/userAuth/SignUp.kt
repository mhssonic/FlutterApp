package com.mhssonic.flutter.ui.userAuth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mhssonic.flutter.R


open class SignUp : Fragment() {
   fun emptyToast(){
        Toast.makeText(
            requireContext(),
            "لطفا اطلاعات مورد نظر را وارد کنید",
            Toast.LENGTH_SHORT
        ).show()
   }
}