package com.mhssonic.flutter.ui.comments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhssonic.flutter.databinding.ActivityShowCommentBinding
import com.mhssonic.flutter.model.CommentsData
import com.mhssonic.flutter.model.MessageIdData
import com.mhssonic.flutter.service.http.RetrofitInstance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ShowCommentActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowCommentBinding
    val compositeDisposable = CompositeDisposable()
    private var comments = CommentsData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowCommentBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(false)

        val id = intent.getSerializableExtra("tweetId") as Int

        val sharedPreferences = getSharedPreferences("cookies", MODE_PRIVATE)

        val serviceApi = RetrofitInstance.getApiService(sharedPreferences)
        val adaptor = RecycleViewComments(comments, serviceApi, this, compositeDisposable)
        binding.recyclerView.adapter = adaptor

        val handler = Handler(Looper.getMainLooper())

        compositeDisposable.add(serviceApi.getComments(MessageIdData(id)).subscribeOn(Schedulers.io()).subscribe({
            comments.clear()
            for(tweet in it){
                comments.add(tweet)
            }
            handler.post {
                adaptor.notifyDataSetChanged()
            }
        }, {
            Log.v("MYTAG", "failed ${it.message!!}")
        }))

        setContentView(binding.root)
    }
}