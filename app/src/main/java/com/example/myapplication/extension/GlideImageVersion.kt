package com.example.myapplication.extension

import java.nio.ByteBuffer
import java.security.MessageDigest

class GlideImageVersion(private var currentVersion: Int) : com.bumptech.glide.load.Key {

    override fun equals(other: Any?): Boolean {
        val o = other as? GlideImageVersion
        o?.let {
            return currentVersion == it.currentVersion
        }
        return false
    }

    override fun hashCode(): Int {
        return currentVersion
    }

    override fun updateDiskCacheKey(md: MessageDigest) {
        md.update(ByteBuffer.allocate(Integer.SIZE).putInt(currentVersion).array())
    }
}