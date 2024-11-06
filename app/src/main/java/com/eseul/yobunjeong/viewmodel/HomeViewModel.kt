package com.eseul.yobunjeong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eseul.yobunjeong.model.HomeModel
import com.eseul.yobunjeong.repository.HomeRepository

class HomeViewModel : ViewModel() {
    private val repository = HomeRepository()

    val homeData: LiveData<HomeModel> = repository.getHomeData()
}
