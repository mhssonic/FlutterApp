package com.mhssonic.flutter.ui.menu

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhssonic.flutter.databinding.FragmentTimeLineBinding
import com.mhssonic.flutter.model.TimeLineData
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class ViewModelTimeLine : ViewModel(){
   var sizeOfTimeLineData = MutableLiveData(0)
}

class TimeLineFragment(val sharedPreferencesCookie: SharedPreferences) : Fragment() {
    private lateinit var binding : FragmentTimeLineBinding
    private val compositeDisposable = CompositeDisposable()
    private var  timeLineData = TimeLineData()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimeLineBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(false)
        val adaptor = RecycleViewTimeLineAdaptor(timeLineData)
        binding.recyclerView.adapter = adaptor

        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)

        val viewModel : ViewModelTimeLine = ViewModelProvider(this)[ViewModelTimeLine::class.java]

        viewModel.sizeOfTimeLineData.observe(viewLifecycleOwner, Observer {
            adaptor.notifyDataSetChanged()
        })

        compositeDisposable.add(serviceApi.getTimeLine().subscribeOn(Schedulers.io()).subscribe({
            for (i in viewModel.sizeOfTimeLineData.value?.until(it.size)!!){
                timeLineData.add(it[i])
            }
            viewModel.sizeOfTimeLineData.postValue(it.size)
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