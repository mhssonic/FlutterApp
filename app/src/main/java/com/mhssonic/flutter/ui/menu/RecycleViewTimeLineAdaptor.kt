package com.mhssonic.flutter.ui.menu

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.Message.MessageData
import com.mhssonic.flutter.model.Message.Tweet.TweetData
import com.mhssonic.flutter.model.Message.getUserDataByUserId
import com.mhssonic.flutter.model.MessageIdData
import com.mhssonic.flutter.model.UserLoginData
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.ApiService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//class ViewModelUserProfile : ViewModel(){
//    var userProfileLiveData : MutableLiveData<UserProfileData> = MutableLiveData()
//}

class RecycleViewTimeLineAdaptor(
    private val timeLineData: ArrayList<MessageData>,
    private val serviceApi: ApiService,
    private val ownerFragment: TimeLineFragment,
    private val compositeDisposable: CompositeDisposable
): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.tweet, parent, false)
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return timeLineData.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = timeLineData[timeLineData.size - position - 1]
        val tweetData = message as TweetData

        holder.text.text = message.text
        holder.like.text = tweetData.likes.toString()
        holder.retweet.text = tweetData.retweet.toString()
        val commentSize = tweetData.comment?.size ?: 0
        holder.comment.text = commentSize.toString()

//        val viewModel : ViewModelUserProfile = ViewModelProvider(ownerFragment)[ViewModelUserProfile::class.java]
        val userProfileLiveData : MutableLiveData<UserProfileData> = MutableLiveData()

        userProfileLiveData.observe(ownerFragment, Observer {it ->
            holder.name.text = "${it.firstName}  ${it.lastName}"
            holder.username.text = it.username
        })
        compositeDisposable.add(serviceApi.getProfileUser(getUserDataByUserId(tweetData.author)).subscribeOn(Schedulers.io()).subscribe({
            userProfileLiveData.postValue(it)
        }, {
            Log.v("MYTAG", "failed ${it.message!!}")
        }))

        holder.imageLike.setOnClickListener {button ->
            button.isEnabled = false

            val call = serviceApi.like(MessageIdData(tweetData.id))
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        if(response.body()?.string() == "SUCCESS"){
                            holder.like.text = tweetData.likes?.plus(1).toString()
                        }else{
                            Toast.makeText(ownerFragment.requireContext(), response.body()?.string(), Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(ownerFragment.requireContext(), "error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    button.isEnabled = true
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    button.isEnabled = true
                    //TODO change it to there is a problem with your network
                    Toast.makeText(ownerFragment.requireContext(), "An error occurred: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    val text : TextView = itemView.findViewById(R.id.tvTweetText)
    val username : TextView = itemView.findViewById(R.id.tvUsername)
    val name : TextView = itemView.findViewById(R.id.tvName)
    val like : TextView = itemView.findViewById(R.id.tvLike)
    val retweet : TextView = itemView.findViewById(R.id.tvRetweet)
    val comment : TextView = itemView.findViewById(R.id.tvComment)

    val imageLike : ImageButton = itemView.findViewById(R.id.imageLike)
    val imageProfile: ImageButton = itemView.findViewById(R.id.ivTweetProfile)

}