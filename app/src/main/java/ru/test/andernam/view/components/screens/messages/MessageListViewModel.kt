package ru.test.andernam.view.components.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.test.andernam.AppModule.provideDatabase
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor() : ViewModel() {

    val storage = provideDatabase()

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO){
            storage.getAllUsers()
        }
    }

    fun selectDialog(dialogHref: String){
        storage.selectDialogHref(dialogHref)
    }

    init {
        getUsers()
    }
}