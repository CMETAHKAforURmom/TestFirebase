package ru.test.andernam.data

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.test.andernam.domain.impl.CloudDatabaseAccessImpl
import ru.test.andernam.old.interfaces.old.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseVariables @Inject constructor() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase: FirebaseFirestore = Firebase.firestore
    var user: FirebaseUser? = auth.currentUser
    val currentDialogHref: MutableState<String> = mutableStateOf("")
    var userPhone: String? = if (user != null) user?.phoneNumber else "+79123456789"
    var localUserInfo: UserInfo = defaultUserInfo(userPhone!!)
    val localUsersMessagingInfo: MutableList<UserInfo> = mutableStateListOf()
    val savedMessagesSnapshot: MutableMap<String, MutableList<Message>> = mutableMapOf()
    private val databaseAccess = CloudDatabaseAccessImpl(firebaseDatabase)

    suspend fun sendMessage(message: String){
        if(userPhone != null)
            databaseAccess.sendMessage(message, userPhone!!, currentDialogHref.value)
    }

    fun getUserDataByDialog(): UserInfo{
        var result: UserInfo = defaultUserInfo("")
        localUsersMessagingInfo.forEach {user ->
            Log.i("Select user is", "$user ${currentDialogHref.value }")
            user.dialogsList.forEach {
                if(currentDialogHref.value in it)
                    result = user
            }
        }
        return result
    }

    fun selectDialogHref(dialogHref: String){
        currentDialogHref.value = dialogHref
        savedMessagesSnapshot[dialogHref] = databaseAccess.getDialogSnapshot(dialogHref)
        Log.i("Navigation with...", savedMessagesSnapshot[dialogHref].toString())
    }
    suspend fun getRecentUsers(){
        databaseAccess.downloadDialogs(localUserInfo).forEach{downloadedUser ->
            var downloadFailed = false
            localUsersMessagingInfo.forEach { existingUser ->
                if(downloadedUser.userId.value == existingUser.userId.value)
                    downloadFailed = true
            }
            if(!downloadFailed)
                localUsersMessagingInfo.add(downloadedUser)
        }
    }

    suspend fun getThisUser() {
        if (userPhone != null) {
            val anotherLocalUserInfo = databaseAccess.downloadProfile(userPhone!!)
            localUserInfo.userId = anotherLocalUserInfo.userId
            localUserInfo.userName.value = anotherLocalUserInfo.userName.value
            localUserInfo.userImageHref.value = anotherLocalUserInfo.userImageHref.value
            localUserInfo.dialogsList = anotherLocalUserInfo.dialogsList
        }
    }

    suspend fun uploadUserInfo(imageHref: Uri, name: String): Result<String>{
        localUserInfo.userName.value = name
        localUserInfo.userImageHref.value = imageHref
        return databaseAccess.uploadUserInfo(imageHref, name, userPhone!!)
    }
    init {
        getUserDataByDialog()
    }
}