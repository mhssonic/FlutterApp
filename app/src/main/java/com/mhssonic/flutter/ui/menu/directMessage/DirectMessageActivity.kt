package com.mhssonic.flutter.ui.menu.directMessage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityDirectMessageBinding
import com.mhssonic.flutter.model.Message.direct.DirectMessageData
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.model.MessageIdData
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.RetrofitInstance
import com.mhssonic.flutter.ui.comments.RecycleViewComments
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DirectMessageActivity(
    ) : AppCompatActivity() {
    private var dataMessages: ArrayList<DirectMessageData> = ArrayList()
    private var liveDataMessages: MutableLiveData<ArrayList<DirectMessageData>> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()
    lateinit var binding: ActivityDirectMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val friendId = intent.getSerializableExtra("friend_id") as Int

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(false)

        val sharedPreferences = getSharedPreferences("cookies", MODE_PRIVATE)

        val serviceApi = RetrofitInstance.getApiService(sharedPreferences)
        val adaptor = RecycleViewDirectMessages(dataMessages , this, friendId, compositeDisposable)//todo check it
        binding.recyclerView.adapter = adaptor

        liveDataMessages.observe(this , Observer {
            val sizeBefore = adaptor.itemCount
            for(i in dataMessages.size until it.size)
                dataMessages.add(it[i])
//            adaptor.notifyDataSetChanged()
            adaptor.notifyItemRangeInserted(sizeBefore, dataMessages.size - sizeBefore)
        })

        compositeDisposable.add(serviceApi.getDirectMessageHttp(getUserDataByUserId(friendId)).subscribeOn(Schedulers.io()).subscribe({
            liveDataMessages.postValue(it)
        }, {
            Log.v("MYTAG", "failed ${it.message!!}")
        }))
    }
}