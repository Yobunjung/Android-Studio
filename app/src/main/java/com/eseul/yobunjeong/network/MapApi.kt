package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.model.MapModel
import retrofit2.http.GET

interface MapApi {
    @GET("/trash_bin_set") // 백엔드 명세에 따라 엔드포인트 작성
    suspend fun getTrashBins(): List<MapModel>
}
