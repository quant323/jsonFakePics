package com.zedevstuds.jsonfakepics.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.zedevstuds.jsonfakepics.model.Album
import com.zedevstuds.jsonfakepics.model.Photo
import com.zedevstuds.jsonfakepics.model.User
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.jvm.Throws

const val TAG = "myLog"

// Константа для передачи Bundle в PhotosFragment
const val USER_ID_BUNDLE = "user_id_bundle"

// Enum состояний загрузки
enum class LoadingStatus { LOADING, ERROR, DONE}

// Константы для формирования URL запроса
const val BASE_URL = "https://jsonplaceholder.typicode.com/"
const val USERS = "users"
const val ALBUMS = "albums"
const val PHOTOS = "photos"
const val USER_ID = "userId"
const val ALBUM_ID = "albumId"

// Загружает json по url и возвращает его
fun getDataFromNetwork(entity: String, queryKey: String = "", vararg queryValues: String = arrayOf("")): String {
    var jsonString = ""
    try {
        val uri = Uri.parse(BASE_URL).buildUpon().appendPath(entity)
        if (queryKey.isNotEmpty()) {
            for (value in queryValues) {
                uri.appendQueryParameter(queryKey, value)
            }
        }
        Log.d(TAG, "Full uri: $uri")
        val requestUrl = URL(uri.toString())

        val urlConnection = requestUrl.openConnection() as (HttpURLConnection)
        urlConnection.requestMethod = "GET"
        urlConnection.connect()

        val inputStream = urlConnection.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        jsonString = reader.readText()

//        urlConnection.disconnect()
//        reader.close()
    } catch (e: IOException) {
        Log.d(TAG, "Error while downloading a json: ${e.message}")
    }
    return jsonString
}

// Загружает изображение по URL и возвращает его как Bitmap
fun getImageFromNetwork(urlString: String): Bitmap? {
    try {
        val requestUrl = URL(urlString)
        val urlConnection = requestUrl.openConnection() as (HttpsURLConnection)
        urlConnection.doInput = true
        urlConnection.requestMethod = "GET"
        urlConnection.setRequestProperty("User-Agent", "jsonFakePics")
        urlConnection.connect()
        val inputStream = urlConnection.inputStream
        return BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        Log.d(TAG, "Error while downloading an image: ${e.message}")
        return null
    }
}

// Парсит json и возвращает список пользователей
fun parseUsers(json: String): List<User> {
    val userList: ArrayList<User> = ArrayList()
    if (json.isNotEmpty()) {
        try {
            val jsonArray = JSONArray(json)
            var id: Long
            var name: String
            for (i in 0 until jsonArray.length()) {
                id = jsonArray.getJSONObject(i).getString("id").toLong()
                name = jsonArray.getJSONObject(i).getString("name")
                val user = User(id, name)
                userList.add(user)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing users: ${e.message}")
        }
    }
    return userList
}

// Парсит json и возвращает список альбомов
fun parseAlbums(json: String): List<Album> {
    val albumsList = ArrayList<Album>()
//    Log.d(TAG, "json is: $json and isNotEmpty = ${json.isNotEmpty()}")
    if (json.isNotEmpty()) {
        try {
            val jsonArray = JSONArray(json)
            var userId: Long
            var id: Long
            for (i in 0 until jsonArray.length()) {
                userId = jsonArray.getJSONObject(i).getString("userId").toLong()
                id = jsonArray.getJSONObject(i).getString("id").toLong()
                val album = Album(userId, id)
                albumsList.add(album)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing albums: ${e.message}")
        }
    }
    return albumsList
}

// Парсит json и возвращает список фото
fun parsePhotos(json: String): List<Photo> {
    val photoList = ArrayList<Photo>()
    if (json.isNotEmpty()) {
        try {
            val jsonArray = JSONArray(json)
            var albumId: Long
            var title: String
            var url: String
            for (i in 0 until jsonArray.length()) {
                albumId = jsonArray.getJSONObject(i).getString("albumId").toLong()
                title = jsonArray.getJSONObject(i).getString("title")
                url = jsonArray.getJSONObject(i).getString("url")
                val photo = Photo(albumId, title, url)
                photoList.add(photo)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing photos: ${e.message}")
        }
    }
    return photoList
}