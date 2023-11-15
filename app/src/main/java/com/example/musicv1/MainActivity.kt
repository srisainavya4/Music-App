package com.example.musicv1

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.lifecycleScope
import com.example.musicv1.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// This is the latest code
// The task
// Navigation Bar - nav_header.xml, nav_menu.xml, activity_main.xml - Linear Layout, Constraint Layout, Drawer Layout, Views - Can do fragment for profile and settings
// Login and Register - Login.kt, Register.kt, MyAPI.kt
// Made changes in repository and nextactivity

class MainActivity : AppCompatActivity() {

    // this is for the navigation bar
    lateinit var toggle: ActionBarDrawerToggle

    // this is for recycler view images my picks
    lateinit var newRecyclerView: RecyclerView
    lateinit var newLatestRecyclerView: RecyclerView
    lateinit var newSecondLatestRecyclerView: RecyclerView

    lateinit var newArrayList: ArrayList<MyPicks>
    lateinit var newArrayLatestList: ArrayList<MyLatest>
    lateinit var newSecondArrayLatestList: ArrayList<MySuggest>

    lateinit var imageId: Array<Int>
    lateinit var imagenewId: Array<Int>
    lateinit var imagesecondnewId: Array<Int>
    lateinit var local: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this section is for the navigation bar
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        //  val toolbar = findViewById<Toolbar>(R.id.idToolbar)

        local = Locale.getDefault().getDisplayLanguage()
        if (local.equals("English")) {
            setLocale("en")
        } else {
            setLocale("hi")
        }
        val actionBar = supportActionBar
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set the width of your custom image to match the default toggle icon
        val defaultToggleIcon = resources.getDrawable(R.drawable.baseline_navigate_before_24, null)
        val toggleIconWidth = defaultToggleIcon.intrinsicWidth
        val toggleIconHeight = defaultToggleIcon.intrinsicHeight
        val customToggleIcon = resources.getDrawable(R.drawable.baseline_person_24, null)
        actionBar?.setHomeAsUpIndicator(customToggleIcon)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageId = arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f
        )
        imagenewId = arrayOf(R.drawable.g, R.drawable.h, R.drawable.i)
        imagesecondnewId = arrayOf(R.drawable.j, R.drawable.k, R.drawable.l)

        newRecyclerView = findViewById(R.id.idLatestCategories)
        newLatestRecyclerView = findViewById(R.id.idSuggestedCategories)
        newSecondLatestRecyclerView = findViewById(R.id.idYourListCategories)

        newRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        newRecyclerView.setHasFixedSize(true)
        newLatestRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        newLatestRecyclerView.setHasFixedSize(true)
        newSecondLatestRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        newSecondLatestRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<MyPicks>()
        newArrayLatestList = arrayListOf<MyLatest>()
        newSecondArrayLatestList = arrayListOf<MySuggest>()

        getUserdata()
        getUserdatatwo()
        getUserdatathree()

        // video view
        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.p
        videoView.setVideoPath(videoPath)
        videoView.start()
        // video view

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
//                R.id.nav_home -> Toast.makeText(applicationContext, "Clicked home",Toast.LENGTH_SHORT).show()
                R.id.nav_home -> {
                    Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
//                R.id.nav_share -> Toast.makeText(
//                    applicationContext,
//                    "Clicked share",
//                    Toast.LENGTH_SHORT
//                ).show()
                R.id.nav_setting -> {
                    Toast.makeText(applicationContext, "Clicked Setting", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, SettingFrame::class.java)
                    startActivity(intent)
                    true
                }
//                R.id.nav_sync -> Toast.makeText(
//                    applicationContext,
//                    "Clicked sync",
//                    Toast.LENGTH_SHORT
//                ).show()
//                R.id.nav_delete -> Toast.makeText(
//                    applicationContext,
//                    "Clicked delete",
//                    Toast.LENGTH_SHORT
//                ).show()
                R.id.nav_login ->  {
                    Toast.makeText(applicationContext, "Clicked Login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                    true
                }
            }
            true
        }
    }

    fun setLocale(local: String) {
        val locale = Locale(local)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

//    fun getUserdata(){
//        val databaseRef = FirebaseDatabase.getInstance().getReference("songs")
//        databaseRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val songs = snapshot.children
//                    songs.forEach { song ->
//                        val title = song.child("title").getValue(String::class.java)
//                        if (title != null) {
//                            Log.d("title", title)
//                        }
//                        val imageUrl = song.child("imageUrl").getValue(String::class.java)
//                        val songUrl = song.child("downloadUrl").getValue(String::class.java)
////                        lifecycleScope.launch(Dispatchers.IO) {
////                            Log.d("I am in getUserdata","time")
////                            val urls = title?.let { Song(title = title, imageUrl =
////                            "https://i.ytimg.com/vi/Z_PODraXg4E/maxresdefault.jpg", songUrl = "https://firebasestorage.googleapis.com/v0/b/mymusic-7c561.appspot.com/o/Ae_Dil_Hai_Mushkil_Title_Song_320_Kbps.mp3?alt=media&token=e70e79c7-d98f-4b03-8b67-247b0ad270cb") }
////                            if (urls != null) {
////                                Log.d("inserting","inserting the url")
////                                AppDatabase.getInstance(applicationContext).urlsDao().insertSong(urls)
////                            }
////                        }
//                        if (imageUrl != null) {
//                            Glide.with(this@MainActivity)
//                                .asBitmap()
//                                .load(imageUrl)
//                                .into(object : CustomTarget<Bitmap>() {
//                                    override fun onResourceReady(
//                                        resource: Bitmap,
//                                        transition: Transition<in Bitmap>?
//                                    ) {
//                                        val mypicks = MyPicks(bitmapToDrawable(resource), songUrl, title)
//                                        newArrayList.add(mypicks)
//                                        Log.d("array", newArrayList.toString())
//                                        newRecyclerView.adapter = MyAdapter(newArrayList)
//                                    }
//
//                                    override fun onLoadCleared(placeholder: Drawable?) {}
//                                })
//                        }
//
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d(TAG, "Failed to read value.", error.toException())
//            }
//        })
//    }
fun getUserdata() = CoroutineScope(Dispatchers.IO).launch {
    val databaseUrl = "https://mymusic-7c561-default-rtdb.firebaseio.com/songs.json"
    val url = URL(databaseUrl)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connect()

    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
        val inputStream = connection.inputStream
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuffer()

        var inputLine = bufferedReader.readLine()
        while (inputLine != null) {
            response.append(inputLine)
            inputLine = bufferedReader.readLine()
        }

        bufferedReader.close()
        inputStream.close()

        withContext(Dispatchers.Main) {
            val songsJson = JSONObject(response.toString())
            val songsIterator = songsJson.keys()

            while (songsIterator.hasNext()) {
                val songKey = songsIterator.next()
                val songJson = songsJson.getJSONObject(songKey)

                val title = songJson.optString("title")
                if (title.isNotEmpty()) {
                    Log.d("title", title)
                }

                val imageUrl = songJson.optString("imageUrl")
                val songUrl = songJson.optString("downloadUrl")

                if (imageUrl.isNotEmpty()) {
                    Glide.with(this@MainActivity)
                        .asBitmap()
                        .load(imageUrl)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                val mypicks = MyPicks(bitmapToDrawable(resource), songUrl, title)
                                newArrayList.add(mypicks)
                                Log.d("array", newArrayList.toString())
                                newRecyclerView.adapter = MyAdapter(newArrayList)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            }
        }
    } else {
        Log.d(TAG, "Failed to retrieve data from Firebase Realtime Database.")
    }
}



    private fun bitmapToDrawable(bitmap: Bitmap): Drawable {
        return BitmapDrawable(resources, bitmap)
    }


    fun deletesongs(){
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getInstance(applicationContext).urlsDao().deleteAllSongs()
        }
    }

    // This is for the suggest row
    // My third adapter uses my suggest
//    fun getUserdatathree(){
//        for (i in imagesecondnewId.indices){
//            val mysuggest = MySuggest(imagesecondnewId[i])
//            newSecondArrayLatestList.add(mysuggest)
//        }
//        newSecondLatestRecyclerView.adapter = MyThirdAdapter(newSecondArrayLatestList)
//    }
    fun getUserdatatwo()
    {
        val databaseRef = FirebaseDatabase.getInstance().getReference("songs")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val songs = snapshot.children
                    songs.forEach { song ->
                        val title = song.child("title").getValue(String::class.java)
                        if (title != null) {
                            Log.d("title", title)
                        }
                        val imageUrl = song.child("imageUrl").getValue(String::class.java)
                        val songUrl = song.child("downloadUrl").getValue(String::class.java)
                        if (imageUrl != null) {
                            Glide.with(this@MainActivity)
                                .asBitmap()
                                .load(imageUrl)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        val mypicks = MyLatest(bitmapToDrawable(resource), songUrl, title)
                                        newArrayLatestList.add(mypicks)
                                        Log.d("array", newArrayLatestList.toString())
                                        newLatestRecyclerView.adapter = MySecondAdapter(newArrayLatestList)
                                    }
                                    override fun onLoadCleared(placeholder: Drawable?) {}
                                })
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    // My list
    fun getUserdatathree() {
        // new part
        lifecycleScope.launch(Dispatchers.IO) {
            val allUrls = AppDatabase.getInstance(applicationContext).urlsDao().getSongs()
            // perform operations on allUrls
            val imageUrls = allUrls.map { it.imageUrl }
            val songUrls = allUrls.map { it.songUrl }
            val title = allUrls.map { it.title }
            Log.d("title", title.toString())
            Log.d("image url in three", imageUrls.toString())
            for (i in imageUrls.indices) {
                if (imageUrls[i] != null) {
                    Glide.with(this@MainActivity)
                        .asBitmap()
                        .load(imageUrls[i])
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                val mypicks = MySuggest(bitmapToDrawable(resource), songUrls[i], title[i])
                                newSecondArrayLatestList.add(mypicks)
                                Log.d("array", newSecondArrayLatestList.toString())
                                newSecondLatestRecyclerView.adapter = MyThirdAdapter(newSecondArrayLatestList)
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            }
        }
//        val allUrls = AppDatabase.getInstance(applicationContext).urlsDao().getSongs()

//        newLatestRecyclerView.adapter = MySecondAdapter(newArrayLatestList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}