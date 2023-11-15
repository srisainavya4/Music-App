package com.example.musicv1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.musicv1.ui.login.LoginCredentials
import com.example.musicv1.ui.login.LoginResponse
import com.example.musicv1.ui.login.MyAPI
import com.example.musicv1.ui.login.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private val api = retrofit.create(MyAPI::class.java)

    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // ...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)
        emailEditText = findViewById(R.id.username_edittext)
        passwordEditText = findViewById(R.id.password_edittext)
        registerButton = findViewById(R.id.register_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val credentials = LoginCredentials(email, password)

            api.login(credentials).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        // Login successful, save the user ID to shared preferences
                        val userId = loginResponse.userId
                        val prefs = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                        prefs.edit().putString("userId", userId).apply()
                        // Go to main activity
                        startActivity(Intent(this@Login, MainActivity::class.java))
                    } else {
                        // Login failed, show an error message
                        if (loginResponse == null){
                            Toast.makeText(this@Login, "Login failed first", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@Login, "Login failed", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Network error, show an error message
                    Toast.makeText(this@Login, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        registerButton.setOnClickListener{
            startActivity(Intent(this@Login, Register::class.java))
        }
    }
}


