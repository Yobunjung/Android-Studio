package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.model.PointModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PointApi {
    @GET("/user/{user_id}/points_logs")
    fun getPointsLogs(
        @Path("user_id") userId: Int
    ): Call<PointModel>
}
