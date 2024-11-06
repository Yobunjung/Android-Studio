package com.eseul.yobunjeong.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eseul.yobunjeong.model.HomeModel
import com.eseul.yobunjeong.network.HomeApi
import com.eseul.yobunjeong.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    private val homeApi = RetrofitClient.instance.create(HomeApi::class.java)

    fun getHomeData(): LiveData<HomeModel> {
        val homeData = MutableLiveData<HomeModel>()

        homeApi.getHomeData().enqueue(object : Callback<HomeModel> {
            override fun onResponse(call: Call<HomeModel>, response: Response<HomeModel>) {
                if (response.isSuccessful) {
                    homeData.value = response.body()
                } else {
                    homeData.value = null // 에러 발생 시 null 처리
                }
            }

            override fun onFailure(call: Call<HomeModel>, t: Throwable) {
                homeData.value = null // 네트워크 오류 시 null 처리
            }
        })

        return homeData
    }
}
