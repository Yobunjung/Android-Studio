package com.eseul.yobunjeong.network

object ApiProvider {
    val cameraApi: CameraApi by lazy {
        RetrofitClient.instance.create(CameraApi::class.java)
    }

    val qrCodeApi: QrCodeApi by lazy {
        RetrofitClient.instance.create(QrCodeApi::class.java)
    }

    val mapApi: MapApi by lazy {
        RetrofitClient.instance.create(MapApi::class.java)
    }
}
