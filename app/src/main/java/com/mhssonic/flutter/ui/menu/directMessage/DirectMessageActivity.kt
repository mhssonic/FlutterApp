package com.mhssonic.flutter.ui.menu.directMessage

import MyWebSocketClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhssonic.flutter.R
import com.mhssonic.flutter.databinding.ActivityDirectMessageBinding
import com.mhssonic.flutter.model.Message.direct.DirectMessageData
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.URI
import java.time.LocalDateTime

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

//        liveDataMessages.observe(this , Observer {
//            val sizeBefore = adaptor.itemCount
//            for(i in dataMessages.size until it.size)
//                dataMessages.add(it[i])
////            adaptor.notifyDataSetChanged()
//            adaptor.notifyItemRangeInserted(sizeBefore, dataMessages.size - sizeBefore)
//        })
//
//        compositeDisposable.add(serviceApi.getDirectMessageHttp(GetUserDataByUserId(friendId)).subscribeOn(Schedulers.io()).subscribe({
//            liveDataMessages.postValue(it)
//        }, {
//            Log.v("MYTAG", "failed ${it.message!!}")
//        }))

        val serverUri = URI(getString(R.string.server_websocket_url))

        val webSocketClient = MyWebSocketClient(serverUri, friendId) {
            val sizeBefore = adaptor.itemCount
            for(obj in it)
                dataMessages.add(obj)
            runOnUiThread {
                run {
                    adaptor.notifyItemRangeInserted(sizeBefore, it.size)
                }
            }
        }
        // connect to websocket server
        webSocketClient.connect()

        binding.sendDirectButton.setOnClickListener {
            val directMessage = DirectMessageData()
            directMessage.reply = -1
            directMessage.targetUser = friendId
            directMessage.text = binding.directMessageText.text.toString()
            directMessage.attachment = ArrayList()
            webSocketClient.send(directMessage)
//            directMessage.postingTime = LocalDateTime.now()
//            directMessage.author = friendId + 1
//            dataMessages.add(directMessage)
//            val sizeBefore = adaptor.itemCount
//            adaptor.notifyItemRangeInserted(sizeBefore, 1)
            binding.directMessageText.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}