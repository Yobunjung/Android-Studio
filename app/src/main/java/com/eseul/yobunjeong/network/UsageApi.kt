package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.model.UsageModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UsageApi {
    @GET("/recycle/{user_id}/logs")
    fun getRecycleLogs(
        @Path("user_id") userId: Int
    ): Call<List<UsageModel>>
}