package com.mhssonic.flutter.ui.menu

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhssonic.flutter.databinding.FragmentTimeLineBinding
import com.mhssonic.flutter.model.TimeLineData
import com.mhssonic.flutter.service.http.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimeLineFragment(val sharedPreferencesCookie: SharedPreferences) : Fragment() {
    private lateinit var binding : FragmentTimeLineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimeLineBinding.inflate(layoutInflater)

//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)

        val call = serviceApi.getTimeLine()
        call.enqueue(object : Callback<TimeLineData> {
            override fun onResponse(call: Call<TimeLineData>, response: Response<TimeLineData>) {
                Log.v("MYTAG", response.body().toString())
            }

            override fun onFailure(call: Call<TimeLineData>, t: Throwable) {
                Log.v("MYTAG", "failed ${t.message!!}")
            }
        })



        return binding.root
    }
}