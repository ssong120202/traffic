package com.example.myapplication.model

import java.io.Serializable

data class TrafficInfo(
        var cameraUrl: CameraUrl = CameraUrl(),
        var quadrant: String = "",
        var camera_location: String = "",
        var point: Point = Point(),
        var isLoading: Boolean = false
) : Serializable