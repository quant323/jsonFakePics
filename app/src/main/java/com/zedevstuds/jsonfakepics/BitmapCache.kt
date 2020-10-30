package com.zedevstuds.jsonfakepics

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache

object BitmapCache {

    private lateinit var memoryCache:  LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 4
        Log.d(TAG, "maxMemory: $maxMemory; cacheSize: $cacheSize")

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                Log.d(TAG, "sizeOfImage: ${value?.byteCount?.div(1024)}")
                return value?.byteCount?.div(1024) ?: 0
            }
        }
    }

    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap)
        }
    }

    fun getBitmapFromMemCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }

}