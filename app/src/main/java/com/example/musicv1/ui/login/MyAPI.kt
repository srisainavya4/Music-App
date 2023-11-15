package com.example.musicv1.ui.login

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface MyAPI {
    @POST("/login")
    fun login(@Body credentials: LoginCredentials): Call<LoginResponse>

    @POST("/register")
    fun register(@Body credentials: RegisterCredentials): Call<RegisterResponse>
}

data class LoginCredentials(val email: String, val password: String)
data class LoginResponse(val success: Boolean, val userId: String)

data class RegisterCredentials(val email: String, val password: String)
data class RegisterResponse(val success: Boolean, val userId: String)

val retrofit = Retrofit.Builder()
    .baseUrl("https://e79a-137-151-175-64.ngrok-free.app/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api = retrofit.create(MyAPI::class.java)
