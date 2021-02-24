package com.example.myapplication.service

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.model.CameraUrl
import com.example.myapplication.model.TrafficInfo
import org.json.JSONArray


class TrafficRepository private constructor(private val application: Application) {
    private val url = "https://data.calgary.ca/resource/k7p9-kppz.json"
    var trafficInfoList = MutableLiveData<List<TrafficInfo>>()

    companion object {
        @Volatile
        private var INSTANCE: TrafficRepository? = null
        fun getInstance(application: Application): TrafficRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TrafficRepository(application).also { INSTANCE = it }
            }
        }
    }

    fun fetchTrafficInfo(context: Context) {
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            if (response != null) {
                val newTrafficInfo = buildTrafficInfo(response)
                trafficInfoList.postValue(newTrafficInfo)
            }
        }, { })
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }

    private fun buildTrafficInfo(response: String): List<TrafficInfo> {
        val trafficList: MutableList<TrafficInfo> = mutableListOf()
        val resultArray = JSONArray(response)
        for (i in 0 until resultArray.length()) {
            val currentObject = resultArray.getJSONObject(i)
            val result = TrafficInfo(
                    camera_location = currentObject.getString(SERVICE_PARAMETERS_CAMERA_LOCATION),
                    quadrant = currentObject.getString(SERVICE_PARAMETERS_QUADRANT),
                    cameraUrl = CameraUrl(
                            url = currentObject.getJSONObject(SERVICE_PARAMETERS_CAMERA_URL).getString(SERVICE_PARAMETERS_URL),
                            description = currentObject.getJSONObject(SERVICE_PARAMETERS_CAMERA_URL).getString(SERVICE_PARAMETERS_DESCRIPTION)
                    )
            )
            trafficList.add(result)
        }
        return trafficList
    }
}