package com.eseul.yobunjeong.repository

import com.eseul.yobunjeong.model.BinStatusModel
import com.eseul.yobunjeong.network.BinStatusApi

class BinRepository(private val binStatusApi: BinStatusApi) {
    suspend fun getBinStatus(): BinStatusModel {
        return binStatusApi.fetchBinStatus()
    }
}

