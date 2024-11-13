package com.eseul.yobunjeong.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QrCodeApi {
    @POST("recycle/create_qr")
    suspend fun createQrCode(@Body requestBody: RequestBody): Response<ResponseBody>
}
