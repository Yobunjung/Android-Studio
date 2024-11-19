package com.eseul.yobunjeong.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eseul.yobunjeong.model.BinStatusModel
import com.eseul.yobunjeong.repository.BinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BinViewModel(private val repository: BinRepository) : ViewModel() {

    private val _binStatus = MutableStateFlow<BinStatusModel?>(null)
    val binStatus: StateFlow<BinStatusModel?> get() = _binStatus

    fun fetchBinStatus() {
        viewModelScope.launch {
            try {
                val response = repository.getBinStatus()
                _binStatus.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                // 오류 처리 로직 추가 가능
            }
        }
    }
}
