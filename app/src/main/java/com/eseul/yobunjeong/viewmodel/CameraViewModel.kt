package com.eseul.yobunjeong.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eseul.yobunjeong.network.RetrofitClient
import com.eseul.yobunjeong.repository.ImageRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.ByteArrayInputStream

class CameraViewModel(private val repository: ImageRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<String>()
    val uploadResult: LiveData<String> get() = _uploadResult

    private val _qrCodeBitmap = MutableLiveData<Bitmap>()
    val qrCodeBitmap: LiveData<Bitmap> get() = _qrCodeBitmap

    // 이미지 업로드 함수
    fun uploadImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImage(image)
                if (response.isSuccessful) {
                    _uploadResult.value = response.body()?.string() ?: "Upload successful"
                } else {
                    _uploadResult.value = "Error: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _uploadResult.value = "Exception: ${e.message}"
            }
        }
    }

    fun requestQrCode(trashType: String, userId: Int) {
        viewModelScope.launch {
            try {
                // JSON 문자열을 생성하여 RequestBody로 변환
                val json = """{"trash_type":"$trashType", "user_id":"$userId"}"""
                val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

                val response = RetrofitClient.qrCodeApi.createQrCode(requestBody)

                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    Log.d("CameraViewModel", "QR Code Response: $responseBody") // 응답 확인용 로그
                    responseBody?.let {
                        val jsonResponse = JSONObject(it)
                        val qrCodeBase64 = jsonResponse.optString("qr_code", "")
                        if (qrCodeBase64.isNotEmpty()) {
                            val qrCodeImage = decodeBase64ToBitmap(qrCodeBase64)
                            if (qrCodeImage != null) {
                                _qrCodeBitmap.value = qrCodeImage
                            } else {
                                Log.e("CameraViewModel", "Failed to decode QR Code")
                            }
                        } else {
                            Log.e("CameraViewModel", "qr_code field is missing in the response.")
                        }
                    }
                } else {
                    Log.e("CameraViewModel", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CameraViewModel", "Exception: ${e.message}")
            }
        }
    }



    // Base64 문자열을 Bitmap으로 디코딩하는 함수
    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val base64Image = base64Str.split(",").getOrNull(1) ?: base64Str
            val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
            BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
        } catch (e: IllegalArgumentException) {
            Log.e("CameraViewModel", "Base64 decoding error: ${e.message}")
            null
        }
    }
}
