package com.eseul.yobunjeong.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eseul.yobunjeong.data.RecycleStatus
import com.eseul.yobunjeong.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecycleViewModel : ViewModel() {

    // Recycle 상태 LiveData
    private val _recycleStatus = MutableLiveData<RecycleStatus>()
    val recycleStatus: LiveData<RecycleStatus> get() = _recycleStatus

    // 오류 메시지 LiveData
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // QR 코드 생성 상태 LiveData
    private val _qrCodeGenerated = MutableLiveData<Boolean>(false)
    val qrCodeGenerated: LiveData<Boolean> get() = _qrCodeGenerated

    // QR 코드 생성 메서드
    fun generateQrCode() {
        Log.d("ViewModel", "QR 코드 생성 중...")

        // QR 코드 생성 완료 상태 업데이트
        _qrCodeGenerated.postValue(true)
        Log.d("ViewModel", "QR 코드 생성 완료")
    }

    // Recycle 상태 API 요청
    fun fetchRecycleStatus() {
        Log.d("ViewModel", "fetchRecycleStatus() 호출됨")
        RetrofitClient.recycleCompleteApi.getRecycleStatus().enqueue(object : Callback<RecycleStatus> {
            override fun onResponse(call: Call<RecycleStatus>, response: Response<RecycleStatus>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("API_SUCCESS", "응답: $it")
                        _recycleStatus.postValue(it) // 상태 업데이트
                    } ?: run {
                        Log.e("API_ERROR", "응답 본문이 비어 있음")
                        _errorMessage.postValue("응답 본문이 비어 있음")
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "알 수 없는 오류"
                    Log.e("API_ERROR", "응답 실패: $error")
                    _errorMessage.postValue(error)
                }
            }

            override fun onFailure(call: Call<RecycleStatus>, t: Throwable) {
                Log.e("API_FAILURE", "네트워크 요청 실패: ${t.message}")
                _errorMessage.postValue("네트워크 요청 실패: ${t.message}")
            }
        })
    }
}
