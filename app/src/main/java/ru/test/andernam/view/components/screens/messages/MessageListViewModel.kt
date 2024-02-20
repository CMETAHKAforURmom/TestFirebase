package ru.test.andernam.view.components.screens.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import ru.test.andernam.AppModule.provideDatabase
import ru.test.andernam.data.UserInfo

class MessageListViewModel : ViewModel() {

    val storage = provideDatabase()

    suspend fun getAll(): MutableList<UserInfo>{
        Log.i("DB from viewmodel", storage.getU().toString())
        return storage.getU()
    }


    suspend fun getUsers() {
        viewModelScope.async {
            storage.getAllUsers()
        }
    }

    init {

    }
}