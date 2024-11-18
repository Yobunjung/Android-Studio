package com.eseul.yobunjeong.repository

import com.eseul.yobunjeong.model.MapModel
import com.eseul.yobunjeong.network.MapApi
import com.eseul.yobunjeong.network.RetrofitClient

class MapRepository {
    private val mapApi: MapApi = RetrofitClient.mapApi

    suspend fun getTrashBins(): List<MapModel> {
        return mapApi.getTrashBins()
    }
}
