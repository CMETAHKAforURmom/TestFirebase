package ru.test.andernam.domain.newest.impl

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.test.andernam.data.UserInfo
import ru.test.andernam.data.defaultUserInfo
import ru.test.andernam.domain.newest.api.CloudDatabaseAccessApi

class CloudDatabaseAccessImpl (private val databaseVariables: FirebaseFirestore): CloudDatabaseAccessApi {
    override suspend fun downloadProfile(userId: String):  UserInfo{
        val taskDownload = databaseVariables.collection("usersData").document(userId).get()
        taskDownload.await()
        return if (taskDownload.isSuccessful && taskDownload.result.exists()){
//            Log.i("DB state" , taskDownload.result.data?.get("clientData")?.toString().toString())
            UserInfo(userId = mutableStateOf(userId),
                mutableStateOf(taskDownload.result.data?.get("clientData")?.toString()?: ""), mutableStateOf(Uri.parse(
                    taskDownload.result.data?.get("profilePhoto").toString())), mutableStateOf(taskDownload.result.data?.get("dialogs")
                    .toString()))
        }

        else {
            databaseVariables.collection("usersData").document(userId)
                .set(mapOf("clientData" to " ", "profilePhoto" to " ", "dialogs" to ""))
            defaultUserInfo(userId)
        }
    }

    suspend fun downloadAllUsers(): MutableList<UserInfo>{
        val userInfoList = mutableListOf<UserInfo>()
        val task = databaseVariables.collection("usersData").get()
        task.await()
        task.result.documents.forEach {
            userInfoList += UserInfo(mutableStateOf(it.id), mutableStateOf(it.data?.get("clientData").toString()), mutableStateOf(
                Uri.parse(it.data?.get("profilePhoto").toString())), mutableStateOf(it.data?.get("dialogs").toString()))
        }
        return userInfoList
    }

    override suspend fun uploadUserInfo(imageHref: Uri, name: String) {

    }

    override suspend fun sendMessage(message: String, userId: String) {
        TODO("Not yet implemented")
    }
}