package com.mhssonic.flutter.ui.menu

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.ApiService
import com.mhssonic.flutter.service.http.DownloadFileService
import com.mhssonic.flutter.ui.menu.directMessage.DirectMessageActivity
import com.mhssonic.flutter.ui.userAuth.Profile.ProfileActivity
import io.reactivex.disposables.CompositeDisposable

class RecycleViewUsersProfileAdaptor(
    private val usersProfileData: ArrayList<UserProfileData>,
    private val serviceApi: ApiService,
    private val ownerFragment: Fragment,
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
        holder.name.text = "${user.firstName} ${user.lastName}"

        val uriProfile : MutableLiveData<Uri> = MutableLiveData()
        DownloadFileService.getFile(serviceApi, user.avatar , compositeDisposable, uriProfile, ownerFragment.requireContext())

        uriProfile.observe(ownerFragment, Observer {
            holder.imageProfile.setImageURI(uriProfile.value)
        })


        holder.view.setOnClickListener{
            moveToDirect(user)
        }

        holder.imageProfile.setOnClickListener{
            moveToProfile(user)
        }
    }

    private fun moveToProfile(user : UserProfileData){
        val intent = Intent(ownerFragment.requireActivity(), ProfileActivity::class.java)
        intent.putExtra("userProfile", user)
        ownerFragment.requireActivity().startActivity(intent)
    }

    private fun moveToDirect(friend : UserProfileData){
        val intent = Intent(ownerFragment.requireActivity(), DirectMessageActivity::class.java)
        intent.putExtra("friend_id", friend.id)
        ownerFragment.requireActivity().startActivity(intent)
    }
}

class MyViewUserProfileHolder(val view: View) : RecyclerView.ViewHolder(view){
    val username : TextView = itemView.findViewById(R.id.user_card_search_username)
    val name: TextView = itemView.findViewById(R.id.user_card_search_name)

    val imageProfile: ImageButton = itemView.findViewById(R.id.user_card_search_image_profile)
}