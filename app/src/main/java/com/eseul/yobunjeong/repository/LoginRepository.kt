package com.eseul.yobunjeong.repository

import com.eseul.yobunjeong.model.LoginRequest
import com.eseul.yobunjeong.model.LoginResponse
import com.eseul.yobunjeong.network.LoginApi
import com.eseul.yobunjeong.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    private val loginApi: LoginApi = RetrofitClient.instance.create(LoginApi::class.java)

    fun loginUser(email: String, password: String, callback: (LoginResponse?) -> Unit) {
        val loginRequest = LoginRequest(email, password)

        loginApi.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null) // 실패한 경우 null 반환
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(null) // 네트워크 오류 시 null 반환
            }
        })
    }
}
