package ru.test.andernam

import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.test.andernam.domain.Message

interface DownloadUploadHelp {

    var idClient: String
    var currentDialog: Any?
    var dialogList: SnapshotStateList<Message>?

    fun rememberDialogPathAndList(dataPair: Pair<Any, SnapshotStateList<Message>>?) {
        if (dataPair != null) {
            currentDialog = dataPair.first
            dialogList = dataPair.second
        }else {
            currentDialog = null
            dialogList = null
        }

    }

    fun rememberThisUser(userId: String) {
        idClient = userId
    }

    fun downloadThisUserInfo(userId: String)

    fun downloadUsersForMessage()

    fun downloadPrewMessages(dialogId: String): SnapshotStateList<Message>

    fun sendMessage(message: String)
}