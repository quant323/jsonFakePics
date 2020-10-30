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
            val userAlbums = getUserAlbums(userId, getAlbums())
 //           getImageTest(getPhotos()[0].url)
            _userPhotos.value = getUserPhotos(userAlbums, getPhotos())
            _status.value = LoadingStatus.DONE
        }
    }

    private suspend fun getAlbums(): List<Album> {
        return withContext(Dispatchers.IO) {
            parseAlbums(getDataFromNetwork(ALBUMS))
        }
    }

    private suspend fun getPhotos(): List<Photo> {
        return withContext(Dispatchers.IO) {
            parsePhotos(getDataFromNetwork(PHOTOS))
        }
    }

//    private suspend fun getImageTest(url: String) {
//        return withContext(Dispatchers.IO) {
//            getImageBitmap(url)
//        }
//    }

}