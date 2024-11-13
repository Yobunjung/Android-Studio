package com.eseul.yobunjeong.repository

import android.util.Log
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

        recycleApi.getRecycleLogs(userId).enqueue(object : Callback<UsageModel> {
            override fun onResponse(call: Call<UsageModel>, response: Response<UsageModel>) {
                if (response.isSuccessful) {
                    val usageModel = response.body()
                    if (usageModel != null) {
                        recycleLogsLiveData.value = listOf(usageModel) // UsageModel을 리스트에 담아 설정
                    }
                    Log.d("UsageRepository", "API 호출 성공: ${response.body()}")
                } else {
                    Log.e("UsageRepository", "API 호출 실패: ${response.code()} - ${response.errorBody()?.string()}")
                    recycleLogsLiveData.value = emptyList()
                }
            }

            override fun onFailure(call: Call<UsageModel>, t: Throwable) {
                Log.e("UsageRepository", "네트워크 오류: ${t.message}")
                recycleLogsLiveData.value = emptyList()
            }
        })

        return recycleLogsLiveData
    }
}
