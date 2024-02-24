package ru.test.andernam.view.components.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.test.andernam.data.DatabaseVariables
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor(val storage: DatabaseVariables) : ViewModel() {

    private fun getUsers() {
        viewModelScope.launch(Dispatchers.IO){
            storage.getRecentUsers()
        }
    }

    fun selectDialog(dialogHref: String){
        storage.selectDialogHref(dialogHref)
    }

    init {
        getUsers()
    }
}