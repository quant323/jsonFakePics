package com.zedevstuds.jsonfakepics

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

const val TAG = "myLog"

// Константа для передачи Bundle во второй фрагмент
const val USER_ID = "user_id"

// Enum состояний загрузки
enum class LoadingStatus { LOADING, ERROR, DONE}

// Константы для формирования URL запроса
const val USERS = "users"
const val ALBUMS = "albums"
const val PHOTOS = "photos"
const val BASE_URL = "https://jsonplaceholder.typicode.com/"

// Загружает json по url и возвращает его
fun getDataFromNetwork(query: String): String {
    val uriString = BASE_URL + query
    Log.d(TAG, uriString)
    val requestUrl = URL(uriString)

    val urlConnection = requestUrl.openConnection() as (HttpURLConnection)
    urlConnection.requestMethod = "GET"
    urlConnection.connect()

    val inputStream = urlConnection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))

    val jsonString = reader.readText()

    urlConnection.disconnect()
    reader.close()
    return jsonString
}

// Загружает изображение по URL и возвращает его как Bitmap
fun getImageBitmap(urlString: String): Bitmap? {
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
        e.printStackTrace()
        Log.d(TAG, "getImageBitmap: ${e.message}")
        return null
    }
}

// Парсит json и возвращает список пользователей
fun parseUsers(json: String): List<User> {
    val jsonArray = JSONArray(json)
    val userList = ArrayList<User>()
    var id: Long
    var name: String
    for (i in 0 until jsonArray.length()) {
        id = jsonArray.getJSONObject(i).getString("id").toLong()
        name = jsonArray.getJSONObject(i).getString("name")
        val user = User(id, name)
        userList.add(user)
    }
    return userList
}

// Парсит json и возвращает список альбомов
fun parseAlbums(json: String): List<Album> {
    val jsonArray = JSONArray(json)
    val albumsList = ArrayList<Album>()
    var userId: Long
    var id: Long
    for (i in 0 until jsonArray.length()) {
        userId = jsonArray.getJSONObject(i).getString("userId").toLong()
        id = jsonArray.getJSONObject(i).getString("id").toLong()
        val album = Album(userId, id)
        albumsList.add(album)
    }
    return albumsList
}

// Парсит json и возвращает список фото
fun parsePhotos(json: String): List<Photo> {
    val jsonArray = JSONArray(json)
    val photoList = ArrayList<Photo>()
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
    return photoList
}

// Возвращает список альбомов, принадлежащих данному пользователю
fun getUserAlbums(userId: Long?, albums: List<Album>): List<Album> {
    val albumList = ArrayList<Album>()
    for (album in albums) {
        if (album.userId == userId) {
            albumList.add(album)
        }
    }
    return albumList
}

// Возвращает список фото, принадлежащих данному пользователю
fun getUserPhotos(albums: List<Album>, photos: List<Photo>): List<Photo> {
    val photoList = ArrayList<Photo>()
    for (album in albums) {
        for (photo in photos) {
            if (photo.albumId == album.id) {
                photoList.add(photo)
            }
        }
    }
    return photoList
}