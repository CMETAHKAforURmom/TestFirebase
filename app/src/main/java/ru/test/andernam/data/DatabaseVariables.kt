package ru.test.andernam.data

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
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
import kotlinx.coroutines.withContext
import ru.test.andernam.data.room.AppDatabase
import ru.test.andernam.data.utilites.toDialogData
import ru.test.andernam.data.utilites.toEntity
import ru.test.andernam.data.utilites.toMap
import ru.test.andernam.data.utilites.toSelfUser
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
    var opponentUser: UserInfo = defaultUserInfo()
    val localUsersMessagingInfo: MutableList<UserInfo> = mutableStateListOf()
    var savedMessagesSnapshot: MutableMap<String, MutableList<Message>> = mutableMapOf()
    private val _localUserFlow = MutableStateFlow<SelfUser>(emptySelfUser())
    val localUserFlow = _localUserFlow.asStateFlow()
    private val _recentUsers = MutableStateFlow<List<UserInfo?>>(emptyList())
    private val _allUsers = MutableStateFlow<List<UserInfo>>(emptyList())
    val allUsers = _allUsers.asStateFlow()
    private val databaseAccess = CloudDatabaseAccessImpl(firebaseDatabase)
    private val coroutine = CoroutineScope(Dispatchers.IO + SupervisorJob())

    suspend fun startMessaging(selectedUser: UserInfo) : String{
        opponentUser = selectedUser
        val recentUsers = localUserFlow.value.messages.keys.toMutableList() as List<UserInfo>
        val names = recentUsers.map { userInfo -> userInfo.userId }
        return if(!names.contains(selectedUser.userId)) withContext(Dispatchers.Default){
            _localUserFlow.value.messages.put(selectedUser, emptyList<Message>())
            var lastDialogAdded = databaseAccess.startNewDialog(localUserFlow.value, selectedUser)
            _localUserFlow.value.dialogs.add(selectedUser.userId+ "|" + lastDialogAdded)
            lastDialogAdded
        }else {
            getDialogByUser(selectedUser)
        }
    }

    suspend fun sendMessage(message: String) {
        if (userUID != null){
            databaseAccess.sendMessage(message, userUID!!, currentDialogHref.value)
        }
    }

    //DataBase!!!
    suspend fun getAllUsers() {
        Firebase.firestore.collection("usersData").get().await().documents.map { doc -> doc.id }
            .forEach { it -> if(it != _localUserFlow.value.userId)
                _allUsers.value += databaseAccess.downloadProfile(it) }
        _allUsers.value.distinct()
    }

    suspend fun getRecentUsers() {
        databaseAccess.downloadDialogs(_localUserFlow.value).forEach { downloadedUser ->
            if (!_recentUsers.value.contains(downloadedUser)) {
                _recentUsers.value += (downloadedUser)
            }
        }
    }

    fun getUserDataByDialog(): UserInfo {
        var result: UserInfo = defaultUserInfo()
        localUsersMessagingInfo.forEach { user ->
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
        coroutine.launch(Dispatchers.IO) {
            savedMessagesSnapshot[dialogHref] = databaseAccess.getDialogSnapshot(dialogHref)
        }
    }

    suspend fun getThisUser() {
        if (userUID != null) {
            try {
                val existingSelfUser = database.selfUserDao().getSelfUser()
                if (existingSelfUser != null) {
                    val dialogs = database.dialogDao().getDialogs()
                    val messages = database.messageDao().getMessages()
                    val allUsers = database.userDao().getAllUsers()
                    val mapUserAndDialog = DialogData(dialogs, messages).toMap(allUsers)
                    _localUserFlow.value = existingSelfUser.toSelfUser(mapUserAndDialog)
                    _localUserFlow.value.messages.forEach { user, messages ->
                        savedMessagesSnapshot += Pair(getDialogByUser(user), messages.toMutableStateList())
                    }

                }
            } catch (e: Exception) {
                Log.e("getThisUser", "Error loading from DB: ${e.message}", e)
            }
            val anotherLocalUserInfo = databaseAccess.downloadProfile(userUID!!)
            _localUserFlow.value.userId = anotherLocalUserInfo.userId
            _localUserFlow.value.userName.value = anotherLocalUserInfo.userName.value
            _localUserFlow.value.userImageHref.value = anotherLocalUserInfo.userImageHref.value
            _localUserFlow.value.dialogs = anotherLocalUserInfo.dialogsList
            _localUserFlow.value.messages =
                anotherLocalUserInfo.dialogsList.flatMap { unSortedDialog ->
                    databaseAccess.downloadUserAndDialogs(
                        unSortedDialog.split("|")[0],
                        unSortedDialog.split("|")[1]
                    ).entries
                }.associate { it.key to it.value }.toMutableMap()

            database.selfUserDao().updateSelfUser(_localUserFlow.value.toEntity())
            val users = _localUserFlow.value.messages.keys.map { user ->
                user.toEntity(_localUserFlow.value.userId)
            }
            val opponentIds = database.userDao().insertUsers(users)

            val opponentIdMap = users.zip(opponentIds).associate { it.first.userId to it.second }

            var dialogData = _localUserFlow.value.toDialogData()

            val dialogs = dialogData.dialogs.mapNotNull { dialog ->
                val opponentUserId = _localUserFlow.value.messages.keys.find { userInfo ->
                    userInfo.dialogsList.any { it.contains(dialog.dialogId) }
                }?.userId ?: return@mapNotNull null
                val opponentId = opponentIdMap[opponentUserId] ?: return@mapNotNull null
                dialog.copy(opponentId = opponentId)
            }
            database.dialogDao().clearDialogs()
            database.dialogDao().updateDialogs(dialogs)
            database.messageDao().clearMessages()
            database.messageDao().updateMessages(dialogData.messages)

        }
    }

    suspend fun uploadUserInfo(imageHref: Uri, name: String): Result<Unit> {
        _localUserFlow.value.userName.value = name
        _localUserFlow.value.userImageHref.value = imageHref
        return databaseAccess.uploadUserInfo(imageHref, name, userUID!!)
    }

    init {
//        getUserDataByDialog()
    }
}