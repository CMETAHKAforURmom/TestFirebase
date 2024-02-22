package ru.test.andernam.view.components.screens.sendMessage

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.test.andernam.AppModule.provideDatabase
import javax.inject.Inject

@HiltViewModel
class SendMessageViewModel @Inject constructor(): ViewModel() {
    val storage = provideDatabase()

    var dialog = storage.savedMessagesSnapshot[storage.currentDialogHref.value]

    init {
        Log.i("String href is...", dialog.toString())
//        if(!href.isNullOrEmpty())
//        viewModelScope.launch {
//            dialog = storage.getDialogMessageElements(href)
//        }
    }
}