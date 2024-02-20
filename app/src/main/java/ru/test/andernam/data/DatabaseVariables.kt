package ru.test.andernam.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.test.andernam.domain.newest.impl.CloudDatabaseAccessImpl
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class DatabaseVariables @Inject constructor() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase: FirebaseFirestore = Firebase.firestore
    var user: FirebaseUser? = auth.currentUser
    var userPhone: String? = "+79515817958"
    var localUserInfo: UserInfo = defaultUserInfo(userPhone!!)
    val localUsersMessagingInfo: MutableList<UserInfo> = mutableStateListOf()
    val downloadState = mutableStateOf(false)
    private val databaseAccess = CloudDatabaseAccessImpl(firebaseDatabase)

    suspend fun getThisUser() {
        if (userPhone != null){
            val anotherLocalUserInfo = databaseAccess.downloadProfile(userPhone!!)
            localUserInfo.userId = anotherLocalUserInfo.userId
            localUserInfo.userName.value = anotherLocalUserInfo.userName.value
            localUserInfo.userImageHref.value = anotherLocalUserInfo.userImageHref.value
            localUserInfo.dialogsList.value = anotherLocalUserInfo.dialogsList.value
        }
    }

    suspend fun getAllUsers(){
        databaseAccess.downloadAllUsers().forEach {
            localUsersMessagingInfo.add(it)
        }
    }

    suspend fun getU(): MutableList<UserInfo>{
        return databaseAccess.downloadAllUsers()
    }

    private suspend fun getUser(userId: String): UserInfo? {
        var userDownload: UserInfo? = null
        localUsersMessagingInfo.forEach {
            userDownload = if (it.userId.value == userId)
                it
            else {
                localUsersMessagingInfo.add(databaseAccess.downloadProfile(userId))
                localUsersMessagingInfo.last()
            }
        }
        return userDownload
    }

    init {
//        if(user != null){
//            userPhone = user!!.phoneNumber
            GlobalScope.async {
                getThisUser()
                getAllUsers()
//                localUserInfo?.dialogsList?.forEach {
//                    getUser(it!!)
//                }
//            }
        }
    }
}