package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.model.LoginRequest
import com.eseul.yobunjeong.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
