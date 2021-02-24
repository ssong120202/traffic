package com.example.myapplication.svgutil

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.engine.GlideException

interface ImageRequestListener {
    fun onError(e: GlideException?)
    fun onSuccess(drawable: Drawable)
}