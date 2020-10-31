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
            if (albumsId.isNotEmpty()) {
                _userPhotos.value = getPhotosInAlbums(albumsId)
                if (userPhotos.value?.isNotEmpty()!!)
                    _status.value = LoadingStatus.DONE
                else _status.value = LoadingStatus.ERROR
            } else {
                _userPhotos.value = ArrayList()
                _status.value = LoadingStatus.ERROR
            }
        }
    }

    // Получает список альбомов выбранного пользователя
    private suspend fun getUserAlbums(userId: Long?): List<Album> {
        return withContext(Dispatchers.IO) {
//            parseAlbums(getDataFromNetwork(""))
            parseAlbums(getDataFromNetwork(ALBUMS, USER_ID, userId.toString()))
        }
    }

    // Получает список фото, находящихся в указанных альбомах
    private suspend fun getPhotosInAlbums(albumsId: List<String>): List<Photo> {
        return withContext(Dispatchers.IO) {
//            parsePhotos(getDataFromNetwork(""))
            parsePhotos(getDataFromNetwork(PHOTOS, ALBUM_ID, *albumsId.toTypedArray()))
        }
    }

}