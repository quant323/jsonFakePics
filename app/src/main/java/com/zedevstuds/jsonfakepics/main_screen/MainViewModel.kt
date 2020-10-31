package com.zedevstuds.jsonfakepics.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.jsonfakepics.model.User
import com.zedevstuds.jsonfakepics.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    // Статус загрузки
    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    fun getUsers() {
        viewModelScope.launch {
            _status.value = LoadingStatus.LOADING
            try {
                _users.value = getUserList()
                _status.value = LoadingStatus.DONE
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = LoadingStatus.ERROR
            }
        }
    }

    // получаем данные из сети и парсим json. На выходе получаем list of Users
    private suspend fun getUserList(): List<User> {
        return withContext(Dispatchers.IO) {
//            parseUsers("")
            parseUsers(getDataFromNetwork(USERS))
        }
    }

}