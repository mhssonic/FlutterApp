package com.mhssonic.flutter.ui.menu

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.FragmentDirectMessagesBinding
import com.mhssonic.flutter.model.Message.UsersProfileData
import com.mhssonic.flutter.model.Message.getUserDataByUsername
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DirectMessagesFragment(private val sharedPreferencesCookie: SharedPreferences) : Fragment() {
    private lateinit var binding : FragmentDirectMessagesBinding
    private val compositeDisposable = CompositeDisposable()
    private val usersProfileData = UsersProfileData()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDirectMessagesBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(false)
        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)

        val adaptor = RecycleViewUsersProfileAdaptor(usersProfileData, serviceApi, this, compositeDisposable)
        binding.recyclerView.adapter = adaptor

        val handler = Handler(Looper.getMainLooper())

        compositeDisposable.add(serviceApi.getFriends().subscribeOn(Schedulers.io()).subscribe({
                usersProfileData.clear()
                for(user in it){
                    usersProfileData.add(user)
                }
                handler.post {
                    adaptor.notifyDataSetChanged()
                }
            }, {
                Log.v("MYTAG", "failed ${it.message!!}")
            }))

        // Inflate the layout for this fragment
        return binding.root
    }
}