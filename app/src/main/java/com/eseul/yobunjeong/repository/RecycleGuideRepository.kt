package com.eseul.yobunjeong.repository

import com.eseul.yobunjeong.model.QuestionRequest
import com.eseul.yobunjeong.model.RecycleResponse
import com.eseul.yobunjeong.network.RecycleGuideApi
import com.eseul.yobunjeong.network.RetrofitClient
import retrofit2.Call

class RecycleGuideRepository {
    private val api: RecycleGuideApi = RetrofitClient.instance.create(RecycleGuideApi::class.java)

    fun getRecycleInfo(question: String): Call<RecycleResponse> {
        val request = QuestionRequest(question)
        return api.getRecycleInfo(request)
    }
}