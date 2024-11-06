package com.eseul.yobunjeong.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eseul.yobunjeong.model.PointModel
import com.eseul.yobunjeong.network.PointApi
import com.eseul.yobunjeong.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PointRepository {
    private val pointApi = RetrofitClient.instance.create(PointApi::class.java)

    fun getPointsLogs(userId: Int): LiveData<PointModel> {
        val pointsData = MutableLiveData<PointModel>()

        pointApi.getPointsLogs(userId).enqueue(object : Callback<PointModel> {
            override fun onResponse(call: Call<PointModel>, response: Response<PointModel>) {
                if (response.isSuccessful) {
                    pointsData.value = response.body()
                } else {
                    pointsData.value = null // 실패 시 null로 설정
                }
            }

            override fun onFailure(call: Call<PointModel>, t: Throwable) {
                pointsData.value = null // 네트워크 오류 시 null로 설정
            }
        })

        return pointsData
    }
}
