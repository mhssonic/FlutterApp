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

        holder.text.text = message.text
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    val text : TextView = itemView.findViewById(R.id.tweetTextTimeLine)
}