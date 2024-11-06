package com.eseul.yobunjeong.network

import com.eseul.yobunjeong.model.QuestionRequest  // 수정된 경로
import com.eseul.yobunjeong.model.RecycleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RecycleGuideApi {
    @POST("/guide")
    fun getRecycleInfo(@Body request: QuestionRequest): Call<RecycleResponse>
}
