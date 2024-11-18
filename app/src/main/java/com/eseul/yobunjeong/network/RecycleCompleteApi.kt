package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.data.RecycleStatus
import retrofit2.Call
import retrofit2.http.GET

interface RecycleCompleteApi {
    @GET("/recycle/1/is_successful")
    fun getRecycleStatus(): Call<RecycleStatus>
}
