package com.example.myapplication.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.model.TrafficInfo
import com.example.myapplication.service.TrafficRepository

class TrafficViewModel(private val trafficRepository: TrafficRepository) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val trafficRepository = TrafficRepository.getInstance(application)
            return TrafficViewModel(trafficRepository) as T
        }
    }

    fun fetchService(context: Context) {
        trafficRepository.fetchTrafficInfo(context)
    }

    fun getSortedList(list: List<TrafficInfo>): List<TrafficInfo> {
        return list.sortedWith(compareBy { it.quadrant })
    }
}