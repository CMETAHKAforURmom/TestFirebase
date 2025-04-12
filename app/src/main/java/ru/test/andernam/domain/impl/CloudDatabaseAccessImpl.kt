package ru.test.andernam.domain.impl

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import ru.test.andernam.data.UserInfo
import ru.test.andernam.data.defaultUserInfo
import ru.test.andernam.domain.api.CloudDatabaseAccessApi
import ru.test.andernam.data.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class CloudDatabaseAccessImpl(private val databaseVariables: FirebaseFirestore) :
    CloudDatabaseAccessApi {

    override suspend fun downloadProfile(userId: String): UserInfo {
        val taskDownload = databaseVariables.collection("usersData").document(userId).get()
        taskDownload.await()
        return if (taskDownload.isSuccessful && taskDownload.result.exists()) {
            UserInfo(
                userId = userId,
                mutableStateOf(taskDownload.result.data?.get("clientData")?.toString() ?: ""),
                mutableStateOf(taskDownload.result.data?.get("profilePhoto").toString().toUri()),
                taskDownload.result.data?.get("dialogs").toString().split(";").toMutableList()
            )
        } else {
            databaseVariables.collection("usersData").document(userId)
                .set(mapOf("clientData" to " ", "profilePhoto" to " ", "dialogs" to ""))
            defaultUserInfo()
        }
    }

    suspend fun downloadUserAndDialogs(
        userId: String,
        dialogsHref: String
    ): Map<UserInfo, List<Message>> {

        return mapOf(downloadProfile(userId) to getDialogSnapshot(dialogsHref).toList())
    }

    override suspend fun startNewDialog(thisUser: UserInfo, opponentUser: UserInfo): String {
        var newDialogUID = UUID.randomUUID().toString()
        try {
            databaseVariables.collection("dialogs").document(newDialogUID).set("initialize" to " ")
            var usersOldDialogs =
                databaseVariables.collection("usersData").document(thisUser.userId.toString()).get()
                    .await().get("dialogs").toString()
            if (usersOldDialogs.isNotEmpty()) {
                usersOldDialogs += ";${opponentUser.userId}|$newDialogUID"
            } else usersOldDialogs = "${opponentUser.userId}|$newDialogUID"
            databaseVariables.collection("usersData").document(thisUser.userId.toString())
                .update("dialogs", usersOldDialogs)
            usersOldDialogs =
                databaseVariables.collection("usersData").document(opponentUser.userId.toString())
                    .get()
                    .await().get("dialogs").toString()
            if (usersOldDialogs.isNotEmpty()) {
                usersOldDialogs += ";${thisUser.userId}|$newDialogUID"
            } else usersOldDialogs = "${thisUser.userId}|$newDialogUID"
            databaseVariables.collection("usersData").document(opponentUser.userId.toString())
                .update("dialogs", usersOldDialogs)

        } catch (e: Exception) {
            Log.i("Creating new dialog error:", e.toString())
        }
        return newDialogUID
    }

    override suspend fun downloadDialogs(localUser: UserInfo): List<UserInfo> {
        val usersList = mutableListOf<UserInfo>()
        if (localUser.dialogsList.isNotEmpty() && localUser.dialogsList.any { it.isNotBlank() } && localUser.dialogsList.any {
                it.contains(
                    "|"
                )
            })
            localUser.dialogsList.forEach {
                usersList.add(downloadProfile(it.split("|")[0]))
            }
        return usersList
    }

    override suspend fun getDialogSnapshot(dialogHref: String): SnapshotStateList<Message> {
        val snapshotMessageDialog: SnapshotStateList<Message> = mutableStateListOf()
        databaseVariables.collection("dialogs").document(dialogHref)
            .addSnapshotListener { snapshot, exception ->
                snapshot?.data?.forEach {
                    try {
                        val newMessage = Message(
                            it.key.split("|")[0],
                            it.key.split("|")[1],
                            it.value.toString(),
                            dialogHref
                        )
                        if (!snapshotMessageDialog.contains(newMessage))
                            snapshotMessageDialog.add(newMessage)
                    } catch (nullException: IndexOutOfBoundsException) {
                        Log.i("UnSuccess when getting messages", nullException.message.toString())
                    }
                }
            }
        return snapshotMessageDialog
    }

    override suspend fun uploadUserInfo(
        imageHref: Uri,
        name: String,
        userId: String
    ): Result<Unit> {
        Firebase.storage.reference.child("$userId/${UUID.randomUUID()}").putFile(imageHref)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { thisUri ->
                    databaseVariables.collection("usersData").document(userId)
                        .update("profilePhoto", thisUri)
                }
            }
        databaseVariables.collection("usersData").document(userId).update("clientData", name)
        return Result.success(Unit)
    }

    override suspend fun sendMessage(message: String, userId: String, messageLink: String) {
        databaseVariables.collection("dialogs").document(messageLink).update(
            "${SimpleDateFormat("yyyy,M,dd hh:mm:ss", Locale.ROOT).format(Date())}|$userId",
            message
        )
    }
}