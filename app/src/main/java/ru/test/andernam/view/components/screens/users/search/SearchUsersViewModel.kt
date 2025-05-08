package ru.test.andernam.view.components.screens.users.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.data.UserInfo
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(val storage: DatabaseVariables) : ViewModel() {
    val allUsers = storage.allUsers

    suspend fun startMessaging(selectedUser: UserInfo) : String{
        return storage.startMessaging(selectedUser)
    }

    fun selectDialog(dialogHref: String){
        storage.selectDialogHref(dialogHref)
    }

}