package ru.test.andernam.view.components.screens.sendMessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.test.andernam.data.DatabaseVariables
import javax.inject.Inject

//@Singleton
@HiltViewModel
class SendMessageViewModel @Inject constructor(val storage: DatabaseVariables): ViewModel() {

    private var dialogs = storage.savedMessagesSnapshot
    var currDialogHref = dialogs[storage.currentDialogHref.value]

    fun sendMessage(message: String){
        viewModelScope.launch {
            storage.sendMessage(message)
        }
    }

    init {
        storage.getUserDataByDialog()
    }
}