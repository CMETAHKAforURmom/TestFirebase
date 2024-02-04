package ru.test.andernam.domain.old.ipl

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.DocumentReference
import ru.test.andernam.domain.old.Message

interface DownloadUploadHelp {

    var DBRequest: DatabaseRequests?
    var idClient: String?
    var currentDialog: DocumentReference?
    var dialogList: SnapshotStateList<Message>?

    fun createDBrequestClass()

    fun rememberDialogPathAndList(dataPair: Pair<DocumentReference, SnapshotStateList<Message>>?) {
        if (dataPair != null) {
            currentDialog = dataPair.first
            dialogList = dataPair.second
        }else {
            currentDialog = null
            dialogList = null
        }
    }

   fun uploadUserInfo(localFileY: Uri, info: String){
       DBRequest?.uploadInfo(localFileY, info, idClient!!)
   }

    fun rememberThisUser(userId: String) {
        idClient = userId
        downloadThisUserInfo(idClient!!)
    }

    fun downloadThisUserInfo(userId: String){
        DBRequest?.downloadUserProfile(userId)
    }

    fun downloadUsersForMessage(userId: String){
//        DBRequest.getAllUsers(userId)
    }

    fun startMessagingWith(opponentId: String){
        rememberDialogPathAndList(DBRequest?.startMessaging(opponentId, idClient!!))
    }
    fun sendMessage(message: String){
        if(currentDialog != null && idClient != null)
            DBRequest?.sendMessage(message, currentDialog!!, idClient!!)
    }
}