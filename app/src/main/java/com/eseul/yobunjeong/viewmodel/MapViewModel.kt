package com.eseul.yobunjeong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eseul.yobunjeong.model.MapModel
import com.eseul.yobunjeong.repository.MapRepository
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val repository = MapRepository()

    private val _trashBins = MutableLiveData<List<MapModel>>()
    val trashBins: LiveData<List<MapModel>> get() = _trashBins

    fun fetchTrashBins() {
        viewModelScope.launch {
            try {
                val bins = repository.getTrashBins()
                _trashBins.value = bins
            } catch (e: Exception) {
                // 에러 처리
                e.printStackTrace()
            }
        }
    }
}
