package com.mhssonic.flutter.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.Message.MessageData
import com.mhssonic.flutter.model.Message.Tweet.RetweetData
import com.mhssonic.flutter.model.Message.Tweet.TweetData
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.model.MessageIdData
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.ApiService
import com.mhssonic.flutter.service.http.DownloadFileService
import com.mhssonic.flutter.ui.comments.MyTweetHolder
import com.mhssonic.flutter.ui.comments.ShowCommentActivity
import com.mhssonic.flutter.ui.userAuth.Profile.ProfileActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDateTime


//class ViewModelUserProfile : ViewModel(){
//    var userProfileLiveData : MutableLiveData<UserProfileData> = MutableLiveData()
//}

class RecycleViewTimeLineAdaptor(
    private val timeLineData: ArrayList<MessageData>,
    private val serviceApi: ApiService,
    private val ownerFragment: TimeLineFragment,
    private val compositeDisposable: CompositeDisposable,
    private var longPressDetected: Boolean = false
): RecyclerView.Adapter<MyTweetHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTweetHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.tweet, parent, false)
        return MyTweetHolder(listItem)
    }

    override fun getItemCount(): Int {
        return timeLineData.size
    }


    override fun onBindViewHolder(holder: MyTweetHolder, position: Int) {
        val message = timeLineData[timeLineData.size - position - 1]
        val tweetData = message as TweetData
        var user = UserProfileData()

        holder.text.text = message.text
        holder.like.text = tweetData.likes.toString()
        holder.retweet.text = tweetData.retweet.toString()
        val commentSize = tweetData.comment?.size ?: 0
        holder.comment.text = commentSize.toString()
        holder.imageProfile.isEnabled = false

        var wholeHashtag = ""
        if(tweetData.hashtag != null){
            for (hashtag in tweetData.hashtag!!){
                if(hashtag != "" && hashtag != " ")
                    wholeHashtag = wholeHashtag.plus("#${hashtag.replace(' ', '_')} ")
            }
        }
        holder.hashtag.text = wholeHashtag

        val currentDateTime = LocalDateTime.now()
        val duration = Duration.between(tweetData.postingTime, currentDateTime)

        var textTime = ""
        if(duration.toDays() != 0L)
            textTime = "${duration.toDays()} روز پیش  "
        else if(duration.toHours() != 0L)
            textTime = "${duration.toDays()} ساعت پیش  "
        else if(duration.toMinutes() != 0L)
            textTime = "${duration.toMinutes()} دقیقه پیش  "
        else
            textTime = "اخیرا"

        holder.time.text = textTime

        val id = if(tweetData is RetweetData)
            tweetData.retweetedMessageId
        else
            tweetData.id

//        val viewModel : ViewModelUserProfile = ViewModelProvider(ownerFragment)[ViewModelUserProfile::class.java]
        val userProfileLiveData : MutableLiveData<UserProfileData> = MutableLiveData()
        val uriProfile : MutableLiveData<Uri> = MutableLiveData()

        userProfileLiveData.observe(ownerFragment, Observer {it ->
            user = it
            holder.name.text = "${it.firstName}  ${it.lastName}"
            holder.username.text = it.username
            holder.imageProfile.isEnabled = true

            DownloadFileService.getFile(serviceApi, user.avatar , compositeDisposable, uriProfile, ownerFragment.requireContext())
        })

        uriProfile.observe(ownerFragment, Observer {
            holder.imageProfile.setImageURI(uriProfile.value)
        })

        compositeDisposable.add(serviceApi.getProfileUser(getUserDataByUserId(tweetData.author)).subscribeOn(Schedulers.io()).subscribe({
            userProfileLiveData.postValue(it)
        }, {
            Log.v("MYTAG", "failed ${it.message!!}")
        }))

        holder.imageProfile.setOnClickListener{
            val intent = Intent(ownerFragment.requireActivity(), ProfileActivity::class.java)
            intent.putExtra("userProfile", user)
            ownerFragment.requireActivity().startActivity(intent)
        }

        holder.imageComment.setOnClickListener{
            val intent = Intent(ownerFragment.requireActivity(), ShowCommentActivity::class.java)
            intent.putExtra("tweetId", id)
            ownerFragment.requireActivity().startActivity(intent)
        }

        val handler = Handler(Looper.getMainLooper())

        val alreadyLikedBeforeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData(holder.alreadyLikedBefore)
        alreadyLikedBeforeMutableLiveData.observe(ownerFragment, Observer {
            if(it)
                holder.imageLike.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.purple))
            else
                holder.imageLike.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.white))
        })

        holder.imageLike.setOnClickListener{
            it.isEnabled = false
            if(holder.alreadyLikedBefore){
                compositeDisposable.add(serviceApi.unlike(MessageIdData(id)).subscribeOn(
                    Schedulers.io()).subscribe({response ->
                    handler.post {
                        it.isEnabled = true
                    }
                    val responseBody = response.string()
                    if(responseBody == "SUCCESS"){
                        handler.post {
                            holder.alreadyLikedBefore = false
                            alreadyLikedBeforeMutableLiveData.postValue(false)
                            holder.like.text = holder.like.text.toString().toInt().plus(-1).toString()
                        }
                    }else {
                        handler.post {
                            Toast.makeText(ownerFragment.requireContext(),responseBody, Toast.LENGTH_SHORT).show()
                        }
                    }
                }, {t ->
                    handler.post {
                        it.isEnabled = true
                        Log.v("MYTAG", "failed ${t.message!!}")
                    }
                }))
            } else{
                compositeDisposable.add(serviceApi.like(MessageIdData(id)).subscribeOn(
                    Schedulers.io()).subscribe({response ->
                    handler.post {
                        it.isEnabled = true
                    }
                    val responseBody = response.string()
                    if(responseBody == "SUCCESS"){
                        handler.post {
                            holder.alreadyLikedBefore = true
                            alreadyLikedBeforeMutableLiveData.postValue(true)
                            holder.like.text = holder.like.text.toString().toInt().plus(1).toString()
                        }
                    }else {
                        handler.post {
                            Toast.makeText(ownerFragment.requireContext(),responseBody, Toast.LENGTH_SHORT).show()
                        }
                    }
                }, {t ->
                    handler.post {
                        it.isEnabled = true
                        Log.v("MYTAG", "failed ${t.message!!}")
                    }
                }))
            }
        }

        holder.imageRetweet.setOnClickListener{button ->
            Log.v("MYTAG", "im short")
            button.isEnabled = false
            compositeDisposable.add(serviceApi.retweet(MessageIdData(id)).subscribeOn(
                Schedulers.io()).subscribe({response ->
                handler.post {
                    button.isEnabled = true
                }
                val responseBody = response.string()
                if(responseBody == "SUCCESS"){
                    handler.post {
                        holder.retweet.text = holder.retweet.text.toString().toInt().plus(1).toString()
                    }
                }else {
                    handler.post {
                        Toast.makeText(ownerFragment.requireContext(),responseBody, Toast.LENGTH_SHORT).show()
                    }
                }
            }, {t ->
                handler.post {
                    button.isEnabled = true
                    Log.v("MYTAG", "failed ${t.message!!}")
                }
            }))
        }

        holder.imageRetweet.setOnLongClickListener {
            Log.v("MYTAG", "im long")
            true
        }


        compositeDisposable.add(serviceApi.alreadyLiked(MessageIdData(id)).subscribeOn(
            Schedulers.io()).subscribe({
            val responseBody = it.string()
            if(responseBody == "true"){
                holder.alreadyLikedBefore = true
                alreadyLikedBeforeMutableLiveData.postValue(true)
            }else {
                holder.alreadyLikedBefore = false
                alreadyLikedBeforeMutableLiveData.postValue(false)
            }
        }, {
            handler.post {
                Log.v("MYTAG", "failed ${it.message!!}")
            }
        }))
    }
    private val longPressRunnable = Runnable {
        longPressDetected = true
        // Do something for long press
    }
}

