package com.example.myapplication.model

import java.io.Serializable

data class Point(
        var type: String = "",
        var coordinates: List<Int> = listOf()
) : Serializable