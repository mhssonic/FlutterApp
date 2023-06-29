package com.mhssonic.flutter.ui.userAuth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mhssonic.flutter.R


class SettingFirst : Fragment() {
    private lateinit var btnChangeFragment: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_first, container, false)

        btnChangeFragment = view.findViewById(R.id.btnSetting)

        btnChangeFragment.setOnClickListener {

            val secondFragment = SettingSecond()

            parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, secondFragment)
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}
