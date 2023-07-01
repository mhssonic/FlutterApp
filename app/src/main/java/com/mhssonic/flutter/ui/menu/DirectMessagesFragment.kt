package com.mhssonic.flutter.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.FragmentDirectMessagesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DirectMessagesFragment : Fragment() {
    lateinit var binding : FragmentDirectMessagesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDirectMessagesBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }
}