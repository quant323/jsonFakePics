package com.zedevstuds.jsonfakepics.photos_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.jsonfakepics.model.Album
import com.zedevstuds.jsonfakepics.model.Photo
import com.zedevstuds.jsonfakepics.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class PhotosViewModel : ViewModel() {

    // Список фотографий пользователя
    private val _userPhotos = MutableLiveData<List<Photo>>()
    val userPhotos: LiveData<List<Photo>>
        get() = _userPhotos

    // Статус загрузки
    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    fun getUserPhotos(userId: Long?) {
        viewModelScope.launch {
            _status.value = LoadingStatus.LOADING
            try {
                // Получаем список альбомов пользователя
                val userAlbums = getUserAlbums(userId)
                // Создаем новый list, содержащий только id альбомов пользователя
                val albumsId: List<String> = userAlbums.map { it.id.toString() }
                _userPhotos.value = getPhotosInAlbums(albumsId)
                _status.value = LoadingStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = LoadingStatus.ERROR
            }
        }
    }

    // Получает список альбомов выбранного пользователя
    private suspend fun getUserAlbums(userId: Long?): List<Album> {
        return withContext(Dispatchers.IO) {
//            parseAlbums(getDataFromNetwork(""))
            parseAlbums(getJSONFromNetwork(ALBUMS, USER_ID, userId.toString()))
        }
    }

    // Получает список фото, находящихся в указанных альбомах
    private suspend fun getPhotosInAlbums(albumsId: List<String>): List<Photo> {
        return withContext(Dispatchers.IO) {
//            parsePhotos(getDataFromNetwork(""))
            parsePhotos(getJSONFromNetwork(PHOTOS, ALBUM_ID, *albumsId.toTypedArray()))
        }
    }

}