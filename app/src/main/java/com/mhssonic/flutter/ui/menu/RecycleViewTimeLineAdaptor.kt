package com.mhssonic.flutter.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.Message.MessageData
import com.mhssonic.flutter.model.Message.Tweet.TweetData
import com.mhssonic.flutter.model.TimeLineData

class RecycleViewTimeLineAdaptor(val timeLineData: ArrayList<MessageData>): RecyclerView.Adapter<MyViewHolder>() {
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
//        holder.username.text = message.author
        holder.like.text = tweetData.likes.toString()
        holder.retweet.text = tweetData.retweet.toString()
        val commentSize = tweetData.comment?.size ?: 0
        holder.comment.text = commentSize.toString()
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    val text : TextView = itemView.findViewById(R.id.tvTweetText)
//    val username : TextView = itemView.findViewById(R.id.tvUsername)
    val name : TextView = itemView.findViewById(R.id.tvName)
    val like : TextView = itemView.findViewById(R.id.tvLike)
    val retweet : TextView = itemView.findViewById(R.id.tvRetweet)
    val comment : TextView = itemView.findViewById(R.id.tvComment)

}