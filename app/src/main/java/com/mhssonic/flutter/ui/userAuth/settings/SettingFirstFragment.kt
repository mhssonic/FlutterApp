package com.mhssonic.flutter.ui.userAuth.settings

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.FragmentSettingFirstBinding
import com.mhssonic.flutter.ui.userAuth.login.LoginActivity

class SettingFirstFragment : Fragment() {
    private lateinit var binding: FragmentSettingFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingFirstBinding.inflate(layoutInflater)
        binding.btnSetting.setOnClickListener {
            val secondFragment = SettingSecondFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, secondFragment)
                .addToBackStack(null)
                .commit()
        }
        val intentLogin = Intent(requireActivity(), LoginActivity::class.java)

        val sharedPreference = requireActivity().getSharedPreferences("cookies", MODE_PRIVATE)

        binding.btnLogout.setOnClickListener{
            it.isEnabled = false
            sharedPreference.edit().remove("token-cookie").commit()
            requireActivity().finish()
            startActivity(intentLogin)
        }

        return binding.root
    }
}
