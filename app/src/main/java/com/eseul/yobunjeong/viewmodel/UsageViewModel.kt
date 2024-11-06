package com.eseul.yobunjeong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eseul.yobunjeong.model.UsageModel
import com.eseul.yobunjeong.repository.UsageRepository

class UsageViewModel : ViewModel() {
    private val repository = UsageRepository()

    fun getRecycleLogs(userId: Int): LiveData<List<UsageModel>> {
        return repository.getRecycleLogs(userId)
    }
}
