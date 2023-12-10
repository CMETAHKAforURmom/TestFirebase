package ru.test.andernam.domain

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.test.andernam.domain.ipl.DatabaseRequests
import ru.test.andernam.view.components.Routes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveUserData @Inject constructor(_ownUserClass: UserClass){

    //Инфа для Model & ViewModel

    private val _stateFlowProfileInfo = MutableStateFlow(ProfileInfo())
    val stateFlowProfileInfo = _stateFlowProfileInfo.asStateFlow()

    fun setProfileInfo(userProfileInfo: ProfileInfo){
        _stateFlowProfileInfo.value = userProfileInfo
    }
    var auth = MutableLiveData<FirebaseAuth?>(_ownUserClass?.auth)
        set(value) {
            field = value
        }
    var user = MutableLiveData<FirebaseUser?>(_ownUserClass?.user)
        set(value){
            field = value
        }
    var phone = MutableLiveData<String?>(_ownUserClass?.idClient)
        set(value) {
            field = value
        }
    var DBRequest = MutableLiveData<DatabaseRequests?>(_ownUserClass?.DBRequest)
        set(value) {
            field = value
        }

    private val _isAuth = MutableStateFlow(false)
    val isAuth = _isAuth.asStateFlow()

    fun changeAuthState(authState: Boolean){
        _isAuth.value = authState
        _screen.value =
            when(isAuth.value){
                true ->  Routes.Main.route
                false -> Routes.Enter.route
            }
    }


    //Только они могут быть вызваны из View
    private val _screen = MutableStateFlow(Routes.Enter.route)
    val screen = _screen.asStateFlow()

    fun changeScreen(getScreen: String){
        _screen.value = getScreen
    }

}