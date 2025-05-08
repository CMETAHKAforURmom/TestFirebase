package ru.test.andernam.view.components.screens.messages.messageList

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.data.UserInfo
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor(val storage: DatabaseVariables) : ViewModel() {

    val recentUsers = storage.localUserFlow.value.messages

    suspend fun startMessaging(selectedUser: UserInfo) : String{
//        storage.opponentUser = selectedUser
//        return if(!recentUsers.contains(selectedUser)) withContext(Dispatchers.Default){
//            Log.i("Start from list", "true")
//            storage.startMessaging(selectedUser)
//        }else {
//            Log.i("Start from list", "false")
//            storage.getDialogByUser(selectedUser)
//        }
        return storage.startMessaging(selectedUser)
    }

    fun selectDialog(dialogHref: String){
        storage.selectDialogHref(dialogHref)
    }
}