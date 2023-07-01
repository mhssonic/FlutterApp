package com.mhssonic.flutter.ui.menu

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.ApiService
import com.mhssonic.flutter.ui.userAuth.Profile.ProfileActivity
import io.reactivex.disposables.CompositeDisposable

class RecycleViewUsersProfileAdaptor(
    private val usersProfileData: ArrayList<UserProfileData>,
    private val serviceApi: ApiService,
    private val ownerFragment: SearchFragment,
    private val compositeDisposable: CompositeDisposable
):  RecyclerView.Adapter<MyViewUserProfileHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewUserProfileHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.user_card_search, parent, false)
        return MyViewUserProfileHolder(listItem)
    }

    override fun getItemCount(): Int {
        return usersProfileData.size
    }

    override fun onBindViewHolder(holder: MyViewUserProfileHolder, position: Int) {
        val user = usersProfileData[position]
        holder.username.text = user.username
        holder.view.setOnClickListener{
            val intent = Intent(ownerFragment.requireActivity(), ProfileActivity::class.java)
            intent.putExtra("user", user)
            ownerFragment.requireActivity().startActivity(intent)

        }
    }
}

class MyViewUserProfileHolder(val view: View) : RecyclerView.ViewHolder(view){
    val username : TextView = itemView.findViewById(R.id.user_card_search_username)
}