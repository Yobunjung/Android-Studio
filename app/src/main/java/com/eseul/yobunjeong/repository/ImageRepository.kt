package com.eseul.yobunjeong.repository

import com.eseul.yobunjeong.network.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

class ImageRepository {
    private val api = RetrofitClient.cameraApi

    suspend fun uploadImage(image: MultipartBody.Part): Response<ResponseBody> {
        return api.uploadImage(image)
    }
}
