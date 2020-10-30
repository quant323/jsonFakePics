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

    fun getUserPhotosss(userId: Long?) {
        viewModelScope.launch {
            _status.value = LoadingStatus.LOADING
            val userAlbums = getUserAlbums(userId, getAlbums(userId))
            val albumsList: List<String> = userAlbums.map {
                it.id.toString()
            }
            _userPhotos.value = getUserPhotos(userAlbums, getPhotos(albumsList))
            _status.value = LoadingStatus.DONE
        }
    }

    private suspend fun getAlbums(userId: Long?): List<Album> {
        return withContext(Dispatchers.IO) {
            parseAlbums(getDataFromNetwork(ALBUMS, USER_ID, userId.toString()))
        }
    }

    private suspend fun getPhotos(albumsId: List<String>): List<Photo> {
        return withContext(Dispatchers.IO) {
            parsePhotos(getDataFromNetwork(PHOTOS, ALBUM_ID, *albumsId.toTypedArray()))
        }
    }

}