package com.example.musicv1
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.jean.jcplayer.model.JcAudio
import com.example.jean.jcplayer.view.JcPlayerView
import com.example.musicv1.db.AppDatabase
import com.example.musicv1.db.Song
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class NextActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var jcplayer: JcPlayerView
    lateinit var jcAudios: ArrayList<JcAudio>
    lateinit var add: Button
    lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_page)
//
        jcplayer = findViewById(R.id.jcplayer)
        jcAudios = ArrayList()

        mediaPlayer = MediaPlayer()
//
//        // this is sqlite
        add = findViewById(R.id.addtolist)
//
//        // Get the data passed in the Intent
        val songTitle = intent.getStringExtra("songTitle")
        val songUrl = intent.getStringExtra("songUrl")
//        val byteArray = intent.getByteArrayExtra("titleImage")

            val databaseRef = FirebaseDatabase.getInstance().getReference("songs")
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val songs = snapshot.children
                        songs.forEach { song ->
                            val title = song.child("title").getValue(String::class.java)
                            if (title != null) {
                                Log.d("title", title)
                                if (title == songTitle){
                                    imageUrl = song.child("imageUrl").getValue(String::class.java)!!
//                                    val songUrl = song.child("downloadUrl").getValue(String::class.java)
                                    if (imageUrl != null) {
                                        Glide.with(this@NextActivity)
                                            .asBitmap()
                                            .load(imageUrl)
                                            .into(object : CustomTarget<Bitmap>() {
                                                override fun onResourceReady(
                                                    resource: Bitmap,
                                                    transition: Transition<in Bitmap>?
                                                ) {
                                                    val imageView = findViewById<CircleImageView>(R.id.circleimageview)
                                                    imageView.setImageBitmap(resource)
                                                }
                                                override fun onLoadCleared(placeholder: Drawable?) {}
                                            })
                                    }
                                }
                            }


                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("fail", "Failed to read value.", error.toException())
                }
            })


//        val bitmap = byteArray?.let { BitmapFactory.decodeByteArray(byteArray, 0, it.size) }
//
//        // Set the values in the TextViews
        val titleTextView = findViewById<TextView>(R.id.musicTitle)
        titleTextView.text = songTitle

        var currentSongUrl: String? = null

//
//        val urlTextView = findViewById<TextView>(R.id.musicArtistName)
//        urlTextView.text = songUrl

//        val imageView = findViewById<CircleImageView>(R.id.circleimageview)
//        imageView.setImageBitmap(bitmap)

        val storageRef = songUrl?.let { Firebase.storage.getReferenceFromUrl(it) }
        val localFile = File(
            getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            songTitle + ".mp3"
        )
//        // Verify that localFile is pointing to a valid location
        if (localFile != null) {
            Log.d("Local file path:", localFile.path)
        } else {
            Log.d("Local file path:", "Local file is null")
        }
//
        if (storageRef != null) {
            storageRef.getFile(localFile).addOnSuccessListener {
                // File downloaded successfully
                // Now, play the song using
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(localFile.path)
                    prepareAsync()
                    setOnPreparedListener {
//                        // Start playing the song when it's prepared
                        Log.d("song",songUrl)
                        jcAudios = ArrayList()
                        val newJcAudio = JcAudio.createFromURL("audio", songUrl)
                        jcAudios.clear()
                        jcAudios.add(newJcAudio)
//                        jcAudios.add(JcAudio.createFromURL("audio",songUrl))
                        jcplayer.initPlaylist(jcAudios,null)
                        jcplayer.playAudio(newJcAudio)
                        jcplayer.createNotification()
                    }
                    }
               }
           .addOnFailureListener {
//                // File download failed
               // Handle the error
                Log.d("song","failed")
           }
        }


        add.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                Log.d("I am in getUserdata","time")
                val urls = title?.let { Song(title = songTitle.toString(), imageUrl = imageUrl.toString(), songUrl = songUrl.toString()) }
                if (urls != null) {
                    Log.d("inserting","inserting the url")
                    AppDatabase.getInstance(applicationContext).urlsDao().insertSong(urls)
                    val mainActivity = MainActivity()
                    mainActivity.getUserdatathree()

                }
            }
        }


    }
    override fun onDestroy() {
        super.onDestroy()
//        // Release the MediaPlayer when the activity is destroyed
    mediaPlayer?.release()
    }
    }


//class NextActivity: AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.second_page)
//
//        // Get the data passed in the Intent
//        val songTitle = intent.getStringExtra("songTitle")
//        val songUrl = intent.getStringExtra("songUrl")
//
//        // Set the values in the TextViews
//        val titleTextView = findViewById<TextView>(R.id.musicTitle)
//        titleTextView.text = songTitle
//
//        val urlTextView = findViewById<TextView>(R.id.musicArtistName)
//        urlTextView.text = songUrl
//    }
//}

//package com.example.musicv1
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
////
//class NextActivity: AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.second_page)
//    }
//}