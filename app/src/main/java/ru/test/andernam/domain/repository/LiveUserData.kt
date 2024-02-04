package ru.test.andernam.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveUserData @Inject constructor(){

    //Инфа для Model & ViewModel

    private val _stateFlowProfileInfo = MutableStateFlow<ProfileInfo?>(null)
    val stateFlowProfileInfo = _stateFlowProfileInfo.asStateFlow()

    fun setProfileInfo(userProfileInfo: ProfileInfo){
        _stateFlowProfileInfo.value = userProfileInfo
    }

    private val _stateFlowScreen = MutableStateFlow("start")
    val stateFlowScreen = _stateFlowScreen.asStateFlow()

    fun setScreen(screen: String){
        _stateFlowScreen.value = screen
    }
}