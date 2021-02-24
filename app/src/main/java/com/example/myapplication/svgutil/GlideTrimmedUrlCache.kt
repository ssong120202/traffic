package com.example.myapplication.svgutil

import com.bumptech.glide.load.model.GlideUrl

class GlideTrimmedUrlCache(url: String?) : GlideUrl(url) {

    override fun getCacheKey(): String {
        val url = toStringUrl()
        return if (url.contains("?")) {
            url.substring(0, url.lastIndexOf("?"))
        } else {
            url
        }
    }
}