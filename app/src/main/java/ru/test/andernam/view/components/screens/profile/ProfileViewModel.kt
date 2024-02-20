package ru.test.andernam.view.components.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.test.andernam.AppModule.provideDatabase
import ru.test.andernam.data.DatabaseVariables
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    var storage: DatabaseVariables = provideDatabase()

    fun exitAccount(){
        storage.auth.signOut()
        storage.user = null
    }

    private suspend fun downloadThisProfile(){
        storage.getThisUser()
    }

    fun saveUserData(imageHref: Uri, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            storage.uploadUserInfo(imageHref, name)
        }
    }
init {
    viewModelScope.launch {
        downloadThisProfile()
    }

}


//    fun exitAccount() {
//        authThingClass.signOut()
//    }
//
//    var nameFromDB = mutableStateOf("")
//    var linkFromDB = mutableStateOf(Uri.EMPTY)
//
//    fun saveUserData() {
//        database.uploadInfo(linkFromDB.value, nameFromDB.value)
//    }
//
//    init {
//        MainScope().launch {
//            liveUserData.stateFlowProfileInfo.collectLatest {
//                nameFromDB.value = it?.name ?: nameFromDB.toString()
//                linkFromDB.value = it?.linkImage
//            }
//        }
//    }
}