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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.test.andernam.data.room.AppDatabase
import ru.test.andernam.data.room.UserDao
import ru.test.andernam.data.utilites.toEntity
import ru.test.andernam.data.utilites.toSelfUser
import ru.test.andernam.data.utilites.transformDialogsToList
import ru.test.andernam.domain.impl.CloudDatabaseAccessImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseVariables @Inject constructor(private val database: AppDatabase) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase: FirebaseFirestore = Firebase.firestore
    var user: FirebaseUser? = auth.currentUser
    val currentDialogHref: MutableState<String> = mutableStateOf("")
    var userUID: String? = user?.uid
    var localUserInfo: UserInfo = defaultUserInfo()
    var opponentUser: UserInfo = defaultUserInfo()
    val localUsersMessagingInfo: MutableList<UserInfo> = mutableStateListOf()
    val savedMessagesSnapshot: MutableMap<String, MutableList<Message>> = mutableMapOf()
    private val _localUserFlow = MutableStateFlow<SelfUser>(emptySelfUser())
    val localUserFlow = _localUserFlow.asStateFlow()
    private val _recentUsers = MutableStateFlow<List<UserInfo?>>(emptyList())
    val recentUsers = _recentUsers.asStateFlow()
    private val databaseAccess = CloudDatabaseAccessImpl(firebaseDatabase)
    private val coroutine = CoroutineScope(Dispatchers.IO + SupervisorJob())

    //Self User!!
    suspend fun startMessaging(selectedUser: UserInfo): String {
        return databaseAccess.startNewDialog(localUserInfo, selectedUser)
    }

//    fun tryGetAllUsersFromDb(): List<UserInfo>{
//        return userDao.getAllUsers()
//    }

//    fun updateUsers(users: List<UserInfo>){
//        coroutine.launch { userDao.insertUsers(users)}
//    }

    suspend fun sendMessage(message: String) {
        if (userUID != null)
            databaseAccess.sendMessage(message, userUID!!, currentDialogHref.value)
    }

    //FLOW!!!
    suspend fun getAllUsers(): MutableList<UserInfo> {
        var usersList: MutableList<UserInfo> = mutableListOf()
        Firebase.firestore.collection("usersData").get().await().documents.map { doc -> doc.id }
            .forEach { it -> usersList.add(databaseAccess.downloadProfile(it)) }
        return usersList.filter { it.userId.toString() != userUID }.toMutableList()
    }

    suspend fun getRecentUsers() {
        databaseAccess.downloadDialogs(localUserInfo).forEach { downloadedUser ->
            if (!_recentUsers.value.contains(downloadedUser)) {
                _recentUsers.value += (downloadedUser)
                Log.i("View Database", "${recentUsers.value}")
            }
            Log.i("View Database with False", "${recentUsers.value}")
        }
        Log.i("View Database after foreach", "${recentUsers.value}")
    }

    fun getUserDataByDialog(): UserInfo {
        var result: UserInfo = defaultUserInfo()
        localUsersMessagingInfo.forEach { user ->
            Log.i("Select user is", "$user ${currentDialogHref.value}")
            user.dialogsList.forEach {
                if (currentDialogHref.value in it)
                    result = user
            }
        }
        return result
    }

    fun getDialogByUser(opponentUser: UserInfo): String {
        val href =
            _localUserFlow.value.dialogs.filter { it.contains(opponentUser.userId.toString()) }[0].split(
                "|"
            )[1]
        return href
    }

    fun selectDialogHref(dialogHref: String) {
        currentDialogHref.value = dialogHref
        savedMessagesSnapshot[dialogHref] = databaseAccess.getDialogSnapshot(dialogHref)
    }

    suspend fun getThisUser() {
        if (userUID != null) {
            if (database.selfUserDao().getSelfUser()?.toSelfUser(emptyMap()) != null)
                _localUserFlow.value =
                    database.selfUserDao().getSelfUser()?.toSelfUser(emptyMap())!!
            val anotherLocalUserInfo = databaseAccess.downloadProfile(userUID!!)
            _localUserFlow.value.userId = anotherLocalUserInfo.userId
            _localUserFlow.value?.userName?.value = anotherLocalUserInfo.userName.value
            _localUserFlow.value?.userImageHref?.value = anotherLocalUserInfo.userImageHref.value
            _localUserFlow.value.dialogs = anotherLocalUserInfo.dialogsList
            _localUserFlow.value.messages =
                anotherLocalUserInfo.dialogsList.flatMap { unSortedDialog ->
                    databaseAccess.downloadUserAndDialogs(
                        unSortedDialog.split("|")[0],
                        unSortedDialog.split("|")[1]
                    ).entries
                }.associate { it.key to it.value }

            Log.i("Mapping new dialogs format is", "${_localUserFlow.value.messages}")
            database.selfUserDao().updateSelfUser(_localUserFlow.value?.toEntity()!!)
//          database.selfUserDao().putMessages(_localUserFlow.value.messages)
//            localUserInfo.dialogsList = anotherLocalUserInfo.dialogsList
        }
    }

    suspend fun uploadUserInfo(imageHref: Uri, name: String): Result<Unit> {
        localUserInfo.userName.value = name
        localUserInfo.userImageHref.value = imageHref
        return databaseAccess.uploadUserInfo(imageHref, name, userUID!!)
    }

    init {
        getUserDataByDialog()
    }
}