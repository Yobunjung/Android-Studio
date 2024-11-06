package com.eseul.yobunjeong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eseul.yobunjeong.model.PointModel
import com.eseul.yobunjeong.repository.PointRepository

class PointViewModel : ViewModel() {
    private val repository = PointRepository()

    // 포인트 데이터를 LiveData로 제공
    fun getPointsLogs(userId: Int): LiveData<PointModel> {
        return repository.getPointsLogs(userId)
    }
}
