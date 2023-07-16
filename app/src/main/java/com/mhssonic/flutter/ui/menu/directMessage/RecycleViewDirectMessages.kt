package com.mhssonic.flutter.ui.menu.directMessage

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mhssonic.flutter.R
import com.mhssonic.flutter.model.Message.direct.DirectMessageData
import io.reactivex.disposables.CompositeDisposable

class RecycleViewDirectMessages(
    private val directMessagesData: ArrayList<DirectMessageData>,
    private val owner: FragmentActivity,
    private val friendId: Int,
    private val compositeDisposable: CompositeDisposable
) :RecyclerView.Adapter<MyDirectMessageHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDirectMessageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.direct_message, parent, false)
        return MyDirectMessageHolder(listItem)
    }

    override fun getItemCount(): Int {
        return directMessagesData.size
    }

    override fun onBindViewHolder(holder: MyDirectMessageHolder, position: Int) {
        val message = directMessagesData[position]

        holder.text.text = message.text

        if(message.author == friendId) {
            val color = ContextCompat.getColor(owner.applicationContext, R.color.your_chat_background)
            holder.textCard.setCardBackgroundColor(color)
        }
        if(position > 0 && message.author == directMessagesData[position - 1].author){
            holder.imageCardProfile.visibility = View.INVISIBLE
            //TODO test if profile should be gone too or not
        }


        val uriProfile : MutableLiveData<Uri> = MutableLiveData()
//        DownloadFileService.getFile(serviceApi, user.avatar , compositeDisposable, uriProfile, owner)

        uriProfile.observe(owner, Observer {
            holder.imageProfile.setImageURI(uriProfile.value)
        })


//        holder.imageProfile.setOnClickListener{
//            val intent = Intent(owner, ProfileActivity::class.java)
//            intent.putExtra("userProfile", user)
//            owner.startActivity(intent)
//        }
    }
}

class MyDirectMessageHolder(val view: View) : RecyclerView.ViewHolder(view){
    val text : TextView = itemView.findViewById(R.id.directMessageText)
    val textCard : CardView = itemView.findViewById(R.id.directMessageTextCard)
    val imageProfile: ImageButton = itemView.findViewById(R.id.directMessageProfile)
    val imageCardProfile : CardView = itemView.findViewById(R.id.directMessageProfileCard)
}