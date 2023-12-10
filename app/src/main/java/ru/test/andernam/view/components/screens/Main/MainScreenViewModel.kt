package ru.test.andernam.view.components.screens.Main

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.test.andernam.domain.AuthThingClass
import ru.test.andernam.domain.LiveUserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainScreenViewModel @Inject constructor(private val liveUserData: LiveUserData,
                                              private val authThingClass: AuthThingClass) : ViewModel(){

//    private var localPair: MutableState<Pair<Uri?, String?>> = mutableStateOf(Pair(Uri.EMPTY, null))
//    private val lo = MainActivity().context
//    lateinit var context: LifecycleOwner
//    @Inject
//    lateinit var dataBaseRequestImpl: DataBaseRequestImpl
//    @Inject
//    lateinit var activity: MainActivity

//    val thisLiveData = dataBaseRequestImpl.downloadUserProfile("+79123456789")
//    fun setPair(userDataPair : Pair<Uri?, String?>){
//        localPair.value = userDataPair
//    }
//
//    val imageLink: MutableLiveData<Uri> by lazy {
//        MutableLiveData<Uri>()
//    }
//    val phoneObserver = Observer<ProfileInfo>{ value ->
//        nameFromDB.value = value.name
//        linkFromDB.value = value.linkImage
//    }

    fun exitAccount(){
        authThingClass.signOut()
    }

    var nameFromDB = mutableStateOf("")
    var linkFromDB = mutableStateOf(Uri.EMPTY)
    init {
        MainScope().launch {
            liveUserData.stateFlowProfileInfo.collectLatest {
                nameFromDB.value = it.name
                linkFromDB.value = it.linkImage
            }
        }
    }
}