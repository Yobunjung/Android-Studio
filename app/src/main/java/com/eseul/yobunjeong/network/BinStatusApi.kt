package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.model.BinStatusModel
import retrofit2.http.GET

interface BinStatusApi {
    @GET("/trash_bin_set/4") // 백엔드 API의 엔드포인트 URL로 변경
    suspend fun fetchBinStatus(): BinStatusModel
}