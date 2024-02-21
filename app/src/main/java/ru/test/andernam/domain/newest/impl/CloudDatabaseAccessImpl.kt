package ru.test.andernam.domain.newest.impl

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import ru.test.andernam.data.UserInfo
import ru.test.andernam.data.defaultUserInfo
import ru.test.andernam.domain.newest.api.CloudDatabaseAccessApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class CloudDatabaseAccessImpl(private val databaseVariables: FirebaseFirestore) :
    CloudDatabaseAccessApi {
    override suspend fun downloadProfile(userId: String): UserInfo {
        val taskDownload = databaseVariables.collection("usersData").document(userId).get()
        taskDownload.await()
        return if (taskDownload.isSuccessful && taskDownload.result.exists()) {
            UserInfo(
                userId = mutableStateOf(userId),
                mutableStateOf(taskDownload.result.data?.get("clientData")?.toString() ?: ""),
                mutableStateOf(Uri.parse(taskDownload.result.data?.get("profilePhoto").toString())),
                mutableStateListOf(taskDownload.result.data?.get("dialogs").toString())
            )
        } else {
            databaseVariables.collection("usersData").document(userId)
                .set(mapOf("clientData" to " ", "profilePhoto" to " ", "dialogs" to ""))
            defaultUserInfo(userId)
        }
    }
    suspend fun downloadDialogs(localUser: UserInfo): List<UserInfo>{
        val usersList = mutableListOf<UserInfo>()
        localUser.dialogsList.forEach {
            usersList.add(downloadProfile(it.split("|")[0]))
        }
        return usersList
    }

    suspend fun downloadAllUsers(): MutableList<UserInfo> {
        val userInfoList = mutableListOf<UserInfo>()
        val task = databaseVariables.collection("usersData").get()
        task.await()
        task.result.documents.forEach {
            userInfoList += UserInfo(
                mutableStateOf(it.id),
                mutableStateOf(it.data?.get("clientData").toString()),
                mutableStateOf(Uri.parse(it.data?.get("profilePhoto").toString())),
                mutableStateListOf(it.data?.get("dialogs").toString())
            )
        }
        return userInfoList
    }

    override suspend fun uploadUserInfo(
        imageHref: Uri,
        name: String,
        userId: String
    ): Result<String> {
        Firebase.storage.reference.child("$userId/${UUID.randomUUID()}").putFile(imageHref)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { thisUri ->
                    databaseVariables.collection("usersData").document(userId)
                        .update("profilePhoto", thisUri)
                }
            }
        databaseVariables.collection("usersData").document(userId).update("clientData", name)
        return Result.success("Success!")
    }

    override suspend fun sendMessage(message: String, userId: String, messageLink: String) {
        databaseVariables.collection("dialogs").document(messageLink).update(
            "${SimpleDateFormat("yyyy,M,dd hh:mm:ss").format(Date())}|$userId", message
        )
    }
}