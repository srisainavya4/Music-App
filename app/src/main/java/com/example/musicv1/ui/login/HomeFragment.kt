//package com.example.musicv1.ui.login
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.example.musicv1.R
//
//class HomeFragment : Fragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.activity_main, container, false)
//    }
//}
package com.example.musicv1.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicv1.*

class HomeFragment : Fragment() {

    lateinit var newRecyclerView: RecyclerView
    lateinit var newLatestRecyclerView: RecyclerView
    lateinit var newSecondLatestRecyclerView:  RecyclerView

    lateinit var newArrayList: ArrayList<MyPicks>
    lateinit var newArrayLatestList: ArrayList<MyLatest>
    lateinit var newSecondArrayLatestList: ArrayList<MySuggest>

    lateinit var imageId : Array<Int>
    lateinit var imagenewId: Array<Int>
    lateinit var imagesecondnewId: Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false);

        val buttonNextPage = view.findViewById<Button>(R.id.button_next_page)


        imageId = arrayOf(R.drawable.a,R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f)
        imagenewId = arrayOf(R.drawable.g,R.drawable.h, R.drawable.i)
        imagesecondnewId = arrayOf(R.drawable.j,R.drawable.k, R.drawable.l)

        newRecyclerView = view.findViewById(R.id.idYourListCategories)
        newLatestRecyclerView = view.findViewById(R.id.idLatestCategories)
        newSecondLatestRecyclerView = view.findViewById(R.id.idSuggestedCategories)

        newRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        newRecyclerView.setHasFixedSize(true)
        newLatestRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        newLatestRecyclerView.setHasFixedSize(true)
        newSecondLatestRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        newSecondLatestRecyclerView.setHasFixedSize(true)


        newArrayList  = arrayListOf<MyPicks>()
        newArrayLatestList = arrayListOf<MyLatest>()
        newSecondArrayLatestList = arrayListOf<MySuggest>()

//        getUserdata()
//        getUserdatatwo()
//        getUserdatathree()

        val videoView = view.findViewById<VideoView>(R.id.videoView)
        val videoPath = "android.resource://" + activity?.packageName + "/" + R.raw.p
        videoView.setVideoPath(videoPath)
        videoView.start()


        return view;
    }
}