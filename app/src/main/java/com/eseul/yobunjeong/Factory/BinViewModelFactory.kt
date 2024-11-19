package com.eseul.yobunjeong.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eseul.yobunjeong.network.RetrofitClient
import com.eseul.yobunjeong.repository.BinRepository
import com.eseul.yobunjeong.viewmodel.BinViewModel

class BinViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BinViewModel::class.java)) {
            val repository = BinRepository(RetrofitClient.binStatusApi) // 올바른 API 사용
            @Suppress("UNCHECKED_CAST")
            return BinViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
