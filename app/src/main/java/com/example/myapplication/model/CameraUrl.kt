package com.example.myapplication.model

import java.io.Serializable

data class CameraUrl(
        var url: String = "",
        var description: String = ""
) : Serializable
