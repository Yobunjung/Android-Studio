package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.model.HomeModel
import retrofit2.Call
import retrofit2.http.GET

interface HomeApi {
    @GET("/user/1/home")
    fun getHomeData(): Call<HomeModel>
}