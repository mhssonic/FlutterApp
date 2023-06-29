package com.mhssonic.flutter.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.Message.Tweet.TweetData

class RecycleViewTimeLineAdaptor(val tweetList : List<TweetData>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.fragment_search, parent, false)
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    }
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){

}