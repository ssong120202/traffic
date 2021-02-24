package com.example.myapplication.extension

import android.text.TextUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.svgutil.GlideApp
import com.example.myapplication.svgutil.GlideTrimmedUrlCache
import java.util.*


fun ImageView.load(_url: String?, _trimURL: Boolean? = true, _placeholder: Int? = null, _strategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC, _error: Int? = null, _options: RequestOptions? = null, _signature: GlideImageVersion? = with(7) {
    val calendar = Calendar.getInstance()
    val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
    val expiry = weekOfYear / this
    GlideImageVersion(expiry)
}) {
    val glide = GlideApp.with(this)
    _url?.let { url ->
        if (!TextUtils.isEmpty(url)) {
            val requestBuilder = if (_trimURL == true) glide.load(GlideTrimmedUrlCache(url))
            else {
                glide.load(url)
            }
            requestBuilder.apply {
                _options?.let { apply(this) }
                _placeholder?.let { placeholder(it) }
                diskCacheStrategy(_strategy)
                _error?.let { error(it) }
                _signature?.let { signature(it) }
                into(this@load)
            }
        } else {
            _placeholder?.let {
                glide.load(ContextCompat.getDrawable(this.context, it)).into(this)
            }
        }
    } ?: run {
        _placeholder?.let {
            glide.load(ContextCompat.getDrawable(this.context, it)).into(this)
        }
    }
}
