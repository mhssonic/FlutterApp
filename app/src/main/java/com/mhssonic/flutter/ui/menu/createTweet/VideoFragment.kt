package com.mhssonic.flutter.ui.menu.createTweet

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mhssonic.flutter.R


class VideoFragment : Fragment() {

    private var videoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_video, container, false)

        arguments?.let {
            videoUri = it.getParcelable("videoUri")
        }

        val cd = view.findViewById<CardView>(R.id.cd)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fbtAdd)

        if (videoUri != null) {
            cd.visibility = View.VISIBLE
            fabAdd.visibility = View.VISIBLE
        }

        val videoView = view.findViewById<VideoView>(R.id.videoView)
        videoView.setVideoURI(videoUri)
        videoView.start()

        val fabRemove = view.findViewById<FloatingActionButton>(R.id.fbtRemove)
        fabRemove.setOnClickListener {
            val parentLayout = view.findViewById<CardView>(R.id.cd)
            fabRemove.visibility = View.GONE
            fabAdd.visibility = View.GONE
            parentLayout.removeView(videoView)
        }

        fabAdd.setOnClickListener {
        }

        return view
    }

}