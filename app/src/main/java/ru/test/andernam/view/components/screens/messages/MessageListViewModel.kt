package ru.test.andernam.view.components.screens.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.data.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MessageListViewModel @Inject constructor(val storage: DatabaseVariables) : ViewModel() {

    private var _allUsers = MutableStateFlow<List<UserInfo>>(emptyList())
    val allUsers = _allUsers.asStateFlow()
    val recentUsers = storage.localUserFlow.value.messages.keys.toMutableList()

    private fun getRecentUsers() {
        viewModelScope.launch{
            storage.getRecentUsers()
        }
    }
    fun getAllUsers(){
        viewModelScope.launch {
            storage.getAllUsers()
        }
    }

    suspend fun startMessaging(selectedUser: UserInfo) : String{
        Log.i("Messages view model", "$selectedUser")
        storage.opponentUser = selectedUser
        return if(!recentUsers.contains(selectedUser)) withContext(Dispatchers.Default){
            storage.startMessaging(selectedUser)
        }else storage.getDialogByUser(selectedUser)
    }

    fun selectDialog(dialogHref: String){
        storage.selectDialogHref(dialogHref)
    }

    init {
        getRecentUsers()
        getAllUsers()
        Log.i("View Model", "${recentUsers}")
    }
}