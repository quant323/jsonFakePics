package com.zedevstuds.jsonfakepics.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.jsonfakepics.model.User
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

    fun getUsers() {
        viewModelScope.launch {
            _users.value = getUserList()
        }
    }

    // получаем данные из сети и парсим json. На выходе получаем list of Users
    private suspend fun getUserList(): List<User> {
        return withContext(Dispatchers.IO) {
            parseUsers(getDataFromNetwork(USERS))
        }
    }

}