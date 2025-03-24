package ru.test.andernam.domain.api

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.test.andernam.data.Message
import ru.test.andernam.data.UserInfo

interface CloudDatabaseAccessApi {

    suspend fun startNewDialog(thisUser: UserInfo, opponentUser: UserInfo): String

    suspend fun downloadDialogs(localUser: UserInfo): List<UserInfo>

    suspend fun downloadProfile(userId: String): UserInfo?

    suspend fun uploadUserInfo(imageHref: Uri, name: String, userId: String): Result<Unit>

    suspend fun sendMessage(message: String, userId: String, messageLink: String)

    fun getDialogSnapshot(dialogHref: String): SnapshotStateList<Message>
}