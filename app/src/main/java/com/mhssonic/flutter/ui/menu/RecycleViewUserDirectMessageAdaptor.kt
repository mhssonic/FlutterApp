package com.mhssonic.flutter.ui.menu

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.UserProfileData
import com.mhssonic.flutter.service.http.ApiService
import com.mhssonic.flutter.service.http.DownloadFileService
import com.mhssonic.flutter.ui.userAuth.Profile.ProfileActivity
import io.reactivex.disposables.CompositeDisposable

class RecycleViewUserDirectMessageAdaptor(
    private val usersProfileData: ArrayList<UserProfileData>,
    private val serviceApi: ApiService,
    private val ownerFragment: DirectMessagesFragment,
    private val compositeDisposable: CompositeDisposable
): RecyclerView.Adapter<MyViewUserDirectMessageHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewUserDirectMessageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.user_card_search, parent, false)
        return MyViewUserDirectMessageHolder(listItem)
    }

    override fun getItemCount(): Int {
        return usersProfileData.size
    }

    override fun onBindViewHolder(holder: MyViewUserDirectMessageHolder, position: Int) {
        val user = usersProfileData[position]
        holder.username.text = user.username
        holder.name.text = "${user.firstName} ${user.lastName}"

        val uriProfile : MutableLiveData<Uri> = MutableLiveData()
        DownloadFileService.getFile(serviceApi, user.avatar , compositeDisposable, uriProfile, ownerFragment.requireContext())

        uriProfile.observe(ownerFragment, Observer {
            holder.imageProfile.setImageURI(uriProfile.value)
        })


        holder.view.setOnClickListener{
            moveToDirectChat(user)
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

    private fun moveToDirectChat(user : UserProfileData){
        val intent = Intent(ownerFragment.requireActivity(), ProfileActivity::class.java)
        intent.putExtra("userProfile", user)
        ownerFragment.requireActivity().startActivity(intent)
    }
}

class MyViewUserDirectMessageHolder(val view: View) : RecyclerView.ViewHolder(view){
    val username : TextView = itemView.findViewById(R.id.user_card_search_username)
    val name: TextView = itemView.findViewById(R.id.user_card_search_name)

    val imageProfile: ImageButton = itemView.findViewById(R.id.user_card_search_image_profile)
}