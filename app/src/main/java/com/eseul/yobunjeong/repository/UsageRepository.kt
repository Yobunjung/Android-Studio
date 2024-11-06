package com.eseul.yobunjeong.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eseul.yobunjeong.model.UsageModel
import com.eseul.yobunjeong.network.RetrofitClient
import com.eseul.yobunjeong.network.UsageApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsageRepository {
    private val recycleApi = RetrofitClient.instance.create(UsageApi::class.java)

    fun getRecycleLogs(userId: Int): LiveData<List<UsageModel>> {
        val recycleLogsLiveData = MutableLiveData<List<UsageModel>>()

        recycleApi.getRecycleLogs(userId).enqueue(object : Callback<List<UsageModel>> {
            override fun onResponse(call: Call<List<UsageModel>>, response: Response<List<UsageModel>>) {
                if (response.isSuccessful) {
                    recycleLogsLiveData.value = response.body()
                } else {
                    recycleLogsLiveData.value = emptyList() // 실패 시 빈 리스트로 설정
                }
            }

            override fun onFailure(call: Call<List<UsageModel>>, t: Throwable) {
                recycleLogsLiveData.value = emptyList() // 네트워크 오류 시 빈 리스트로 설정
            }
        })

        return recycleLogsLiveData
    }
}