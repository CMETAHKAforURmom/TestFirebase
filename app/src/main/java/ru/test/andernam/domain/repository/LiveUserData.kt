package ru.test.andernam.domain.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveUserData @Inject constructor(){

    //Инфа для Model & ViewModel

    private val _stateFlowProfileInfo = MutableStateFlow<ProfileInfo?>(null)
    val stateFlowProfileInfo = _stateFlowProfileInfo.asStateFlow()

    private val _isAuthPassed = MutableStateFlow<Boolean>(false)
    val isAuthPassed = _isAuthPassed.asStateFlow()


    fun setAuthPassed(){
        _isAuthPassed.value = true
    }
    fun setProfileInfo(userProfileInfo: ProfileInfo){
        _stateFlowProfileInfo.value = userProfileInfo
    }

    private val _stateFlowScreen = MutableStateFlow("start")
    val stateFlowScreen = _stateFlowScreen.asStateFlow()

    fun setScreen(screen: String){
        _stateFlowScreen.value = screen
    }
}