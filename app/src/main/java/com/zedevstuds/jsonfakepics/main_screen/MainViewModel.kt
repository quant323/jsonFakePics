package com.zedevstuds.jsonfakepics.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.jsonfakepics.model.User
import com.zedevstuds.jsonfakepics.utils.LoadingStatus
import com.zedevstuds.jsonfakepics.utils.USERS
import com.zedevstuds.jsonfakepics.utils.getDataFromNetwork
import com.zedevstuds.jsonfakepics.utils.parseUsers
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
            _users.value = getUserList()
            if (users.value?.isNotEmpty()!!)
                _status.value = LoadingStatus.DONE
            else _status.value = LoadingStatus.ERROR
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