package com.mhssonic.flutter.ui.menu

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mhssonic.flutter.databinding.FragmentTimeLineBinding
import com.mhssonic.flutter.model.TimeLineData
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimeLineFragment(val sharedPreferencesCookie: SharedPreferences) : Fragment() {
    private lateinit var binding : FragmentTimeLineBinding

    private val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimeLineBinding.inflate(layoutInflater)

//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)

        compositeDisposable.add(serviceApi.getTimeLine().subscribeOn(Schedulers.io()).subscribe({
            Log.v("MYTAG", it.first().toString())
        }, {
            Log.v("MYTAG", "failed ${it.message!!}")
        }))



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}