package com.example.musicv1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.musicv1.ui.login.ProfileDetailsFragment
import com.example.musicv1.ui.login.Settings

class SettingFrame : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame)

        val profile = findViewById<Button>(R.id.profile)
        val setting = findViewById<Button>(R.id.setting)
        val home = findViewById<Button>(R.id.home)

        replaceFragment(Settings())

        profile.setOnClickListener{
            replaceFragment(ProfileDetailsFragment())
        }

        setting.setOnClickListener{
            replaceFragment(Settings())
        }
        home.setOnClickListener{
            Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SettingFrame, MainActivity::class.java)
            startActivity(intent)
            true
        }

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout1,fragment)
        fragmentTransaction.commit()
    }
}