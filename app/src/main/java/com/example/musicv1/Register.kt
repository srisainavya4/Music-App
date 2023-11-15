package com.example.musicv1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.musicv1.ui.login.MyAPI
import com.example.musicv1.ui.login.RegisterCredentials
import com.example.musicv1.ui.login.RegisterResponse
import com.example.musicv1.ui.login.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    private val api = retrofit.create(MyAPI::class.java)

    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.register_button)
        emailEditText = findViewById(R.id.email_edittext)
        usernameEditText = findViewById(R.id.username_edittext)
        passwordEditText = findViewById(R.id.password_edittext)
        confirmPasswordEditText = findViewById(R.id.confirm_password_edittext)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                val credentials = RegisterCredentials(email,password)

                api.register(credentials).enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        val registerResponse = response.body()
                        if (registerResponse != null && registerResponse.success) {
                            // Registration successful, save the user ID to shared preferences
                            val userId = registerResponse.userId
                            val prefs = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                            prefs.edit().putString("userId", userId).apply()
                            // Go to main activity
                            startActivity(Intent(this@Register, MainActivity::class.java))
                        } else {
                            // Registration failed, show an error message
                            if (registerResponse == null){
                                Toast.makeText(this@Register, "Registration failed first", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(this@Register, "Registration failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@Register, "Registration failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
