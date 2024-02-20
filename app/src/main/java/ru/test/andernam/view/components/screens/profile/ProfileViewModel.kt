package ru.test.andernam.view.components.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.test.andernam.AppModule.provideDatabase
import ru.test.andernam.data.DatabaseVariables

//@Singleton
class ProfileViewModel : ViewModel() {

//    private val storage = provideDatabase()

    var storage: DatabaseVariables = provideDatabase()

    fun exitAccount(){
        storage.auth.signOut()
        storage.user = null
    }

    fun saveUserData(){
        Log.i("DB state" , storage.localUserInfo.userName.value.toString())
    }
init {
//    Log.i("DB state" , storage.localUserInfo.userName.value.toString())
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