package ru.test.andernam.view.components.screens.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.test.andernam.AppModule.provideDatabase
import ru.test.andernam.data.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MessageListViewModel @Inject constructor() : ViewModel() {

    val storage = provideDatabase()

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO){
            storage.getAllUsers()
        }
    }

    init {
        getUsers()
    }
}