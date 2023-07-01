package com.mhssonic.flutter.ui.userAuth.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mhssonic.flutter.R

class SettingFirst : Fragment() {
    private lateinit var btnChangeFragment: Button
    private lateinit var btnPreFragment: Button

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

        btnPreFragment = view.findViewById(R.id.btnPrev)
        btnPreFragment.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        return view
    }
}
