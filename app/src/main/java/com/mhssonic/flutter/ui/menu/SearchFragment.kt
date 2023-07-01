package com.mhssonic.flutter.ui.menu

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhssonic.flutter.databinding.FragmentSearchBinding
import com.mhssonic.flutter.model.Message.UsersProfileData
import com.mhssonic.flutter.model.Message.getUserDataByUsername
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import android.os.Handler
import android.os.Looper

class SearchFragment(private val sharedPreferencesCookie: SharedPreferences) : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private val compositeDisposable = CompositeDisposable()
    private val usersProfileData = UsersProfileData()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(false)
        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)

        val adaptor = RecycleViewUsersProfileAdaptor(usersProfileData, serviceApi, this, compositeDisposable)
        binding.recyclerView.adapter = adaptor


        val handler = Handler(Looper.getMainLooper())

        binding.buttonSearch.setOnClickListener {
            binding.buttonSearch.isEnabled = false
            compositeDisposable.add(serviceApi.searchUsersProfile(getUserDataByUsername(binding.textSearch.text.toString()))
                .subscribeOn(Schedulers.io()).subscribe({
                    usersProfileData.clear()
                    for(user in it){
                        usersProfileData.add(user)
                    }
                    handler.post {
                        binding.buttonSearch.isEnabled = true
                        adaptor.notifyDataSetChanged()
                    }
                }, {
                    handler.post {
                        binding.buttonSearch.isEnabled = true
                    }
                    Log.v("MYTAG", "failed ${it.message!!}")
                }))
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}