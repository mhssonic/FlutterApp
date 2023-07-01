package com.mhssonic.flutter.ui.menu.createTweet

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mhssonic.flutter.R


class ImageFragment : Fragment() {

    private var imageUri: Uri? = null
    private var count = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_image, container, false)

        arguments?.let {
            imageUri = it.getParcelable("imageUri")
        }

        val cd = view.findViewById<CardView>(R.id.cd)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fbtAdd)


        if (imageUri != null){
            count++
            cd.visibility = VISIBLE
            fabAdd.visibility = VISIBLE

        }

        var imageName = "imageView$count"



        val imageView = view.findViewById<ImageView>(R.id.imageView)
        imageView.setImageURI(imageUri)

        val fabRemove = view.findViewById<FloatingActionButton>(R.id.fbtRemove)
        fabRemove.setOnClickListener {
            val parentLayout = view.findViewById<CardView>(R.id.cd)
            fabRemove.visibility = GONE
            fabAdd.visibility = GONE
            parentLayout.removeView(imageView)
        }

        fabAdd.setOnClickListener {
        }

        return view
    }
}