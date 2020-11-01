package com.zedevstuds.jsonfakepics.utils

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache

// Работа с Кэшем
object BitmapCache {

    private var memoryCache:  LruCache<String, Bitmap>

    init {
        // Определяем максимальную память JVM
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        Log.d(TAG, "max memory: $maxMemory")
        // Задаем размер Кэша
        val cacheSize = maxMemory / 4
        Log.d(TAG, "cache size: $cacheSize")

        // Создаем объект LruCache
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                Log.d(TAG, "image_size: ${value?.byteCount?.div(1024)}")
                return value?.byteCount?.div(1024) ?: 0
            }
        }
    }

    // Добавляет изображение в кэш
    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap)
        }
    }

    // Достает изображение из кэша
    fun getBitmapFromMemCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }

}