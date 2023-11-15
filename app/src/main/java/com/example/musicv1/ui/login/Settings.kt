package com.example.musicv1.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.musicv1.R
import com.google.android.material.navigation.NavigationView

class Settings : Fragment() {
    @SuppressLint("MissingInflatedId")
    lateinit var toggle: ActionBarDrawerToggle
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings, container, false)

        val nightMdeToggle_btn = view.findViewById<Switch>(R.id.nightMdeToggle_btn);

        val sharedPreference =  activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val isNightMode = sharedPreference?.getString("isNightMode","disabled");

        if(isNightMode.equals("enable")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            nightMdeToggle_btn.isChecked = true
        }


        nightMdeToggle_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val sharedPreference = activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                var editor = sharedPreference!!.edit()
                if(nightMdeToggle_btn.isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putString("isNightMode","enable")
                    editor.commit()
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putString("isNightMode","disabled")
                    editor.commit()
                }
            }
        })

        return view;
    }
}