package com.zedevstuds.jsonfakepics.utils

import android.graphics.Bitmap
import android.util.LruCache

// Работа с Кэшем
object BitmapCache {

    private var memoryCache:  LruCache<String, Bitmap>

    init {
        // Опререляем максимальную память JVM
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        // Задаем размер Кэша
        val cacheSize = maxMemory / 4

        // Создаем объект LruCache
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
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