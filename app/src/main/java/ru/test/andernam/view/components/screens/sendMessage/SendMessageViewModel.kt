package ru.test.andernam.view.components.screens.sendMessage

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.test.andernam.data.DatabaseVariables
import javax.inject.Inject

//@Singleton
@HiltViewModel
class SendMessageViewModel @Inject constructor(private val storage: DatabaseVariables): ViewModel() {


    var dialogs = storage.savedMessagesSnapshot
    var currDialogHref = dialogs["a8522802-68cc-47d0-909f-6f725e828ae1"]

    fun getData(){
        Log.i("String href is...", "${storage.currentDialogHref.value} ${storage.savedMessagesSnapshot.size}")
        storage.getMessages()
    }

    init {
        Log.i("String href is...", "${currDialogHref.toString()} ${dialogs.size}")
//        if(!href.isNullOrEmpty())
//        viewModelScope.launch {
//            dialog = storage.getDialogMessageElements(href)
//        }
    }
}