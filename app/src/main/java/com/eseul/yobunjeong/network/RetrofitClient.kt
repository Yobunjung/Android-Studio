package com.eseul.yobunjeong.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://3.38.54.56:5000"

    // Retrofit 인스턴스를 직접 제공
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 또는 API를 바로 사용할 수 있도록 CameraApi 제공
    val cameraApi: CameraApi by lazy {
        instance.create(CameraApi::class.java)
    }

    // QrCodeApi 인터페이스 인스턴스 생성
    val qrCodeApi: QrCodeApi by lazy {
        instance.create(QrCodeApi::class.java)
    }
}
