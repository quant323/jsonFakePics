package com.zedevstuds.jsonfakepics.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.jsonfakepics.*
import com.zedevstuds.jsonfakepics.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    fun getUsers() {
        viewModelScope.launch {
            _users.value = getUserList()
//            Log.d(TAG, "Thread is: ${Thread.currentThread().name}")
        }
    }

    // получаем данные из сети и парсим json. На выходе получаем list of Users
    private suspend fun getUserList(): List<User> {
        return withContext(Dispatchers.IO) {
            parseUsers(getDataFromNetwork(USERS))
        }
    }

}