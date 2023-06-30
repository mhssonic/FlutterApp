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


class ViewModelUsersProfile : ViewModel(){
    var usersProfileLiveData : MutableLiveData<UsersProfileData> = MutableLiveData(UsersProfileData())
}
class SearchFragment(private val sharedPreferencesCookie: SharedPreferences) : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel : ViewModelUsersProfile
    private val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(false)
        val serviceApi = RetrofitInstance.getApiService(sharedPreferencesCookie)
        //TODO check the !!

        viewModel = ViewModelProvider(requireActivity())[ViewModelUsersProfile::class.java]

        val adaptor = RecycleViewUsersProfileAdaptor(viewModel.usersProfileLiveData.value!!, serviceApi, this, compositeDisposable)
        binding.recyclerView.adapter = adaptor

        viewModel.usersProfileLiveData.observe(viewLifecycleOwner, Observer {
            adaptor.notifyDataSetChanged()
        })

        compositeDisposable.add(serviceApi.searchUsersProfile(getUserDataByUsername("")).subscribeOn(Schedulers.io()).subscribe({

            viewModel.usersProfileLiveData.postValue(it)
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