package com.mhssonic.flutter.ui.userAuth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhssonic.flutter.R


class SettingSecond : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("MYTAG" , "::::::::::::::::::kmm::::::::::::::::::::")

        val view = inflater.inflate(R.layout.fragment_setting_second, container, false)
        Log.i("MYTAG" , "::::::::::::::::::::::::::::::::::::::")

        return view
    }
}