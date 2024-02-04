package ru.test.andernam.view.components.screens.Main

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.test.andernam.AppModule.provideAuthClass
import ru.test.andernam.AppModule.provideDB
import ru.test.andernam.AppModule.provideUserLiveData
import ru.test.andernam.domain.AuthThingClass
import ru.test.andernam.domain.DBFirstStep
import ru.test.andernam.domain.repository.LiveUserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val liveUserData: LiveUserData = provideUserLiveData()
    private val authThingClass: AuthThingClass = provideAuthClass()
    private val database: DBFirstStep = provideDB()

    fun exitAccount() {
        authThingClass.signOut()
    }

    var nameFromDB = mutableStateOf("")
    var linkFromDB = mutableStateOf(Uri.EMPTY)

    fun saveUserData() {
        database.uploadInfo(linkFromDB.value, nameFromDB.value)
    }

    init {
        MainScope().launch {
            liveUserData.stateFlowProfileInfo.collectLatest {
                nameFromDB.value = it?.name ?: nameFromDB.toString()
                linkFromDB.value = it?.linkImage
            }
        }
    }
}