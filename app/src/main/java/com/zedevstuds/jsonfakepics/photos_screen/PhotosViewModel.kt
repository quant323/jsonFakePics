package com.zedevstuds.jsonfakepics.photos_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.jsonfakepics.*
import com.zedevstuds.jsonfakepics.model.Album
import com.zedevstuds.jsonfakepics.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            // Получаем список альбомов пользователя
            val userAlbums = getUserAlbums(userId)
            // Создаем новый list, содержащий только id альбомов пользователя
            val albumsId: List<String> = userAlbums.map {
                it.id.toString()
            }
            // Получаем список фото пользователя, по id альбомов, имеющихся у пользователя
            _userPhotos.value = getPhotosInAlbums(albumsId)
            _status.value = LoadingStatus.DONE
        }
    }

    // Получает список альбомов выбранного пользователя
    private suspend fun getUserAlbums(userId: Long?): List<Album> {
        return withContext(Dispatchers.IO) {
            parseAlbums(getDataFromNetwork(ALBUMS, USER_ID, userId.toString()))
        }
    }

    // Получает список фото, находящихся в указанных альбомах
    private suspend fun getPhotosInAlbums(albumsId: List<String>): List<Photo> {
        return withContext(Dispatchers.IO) {
            parsePhotos(getDataFromNetwork(PHOTOS, ALBUM_ID, *albumsId.toTypedArray()))
        }
    }

}