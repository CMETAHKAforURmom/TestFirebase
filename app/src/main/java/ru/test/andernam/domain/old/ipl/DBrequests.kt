package ru.test.andernam.domain.old.ipl

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ru.test.andernam.domain.old.Message

interface DatabaseRequests {

    var database: FirebaseFirestore
    var storage: FirebaseStorage
    var messageLink: DocumentReference
    var docExist: Boolean
    var clientDialogsList: String
    var dbState: Task<DocumentSnapshot>

    fun uploadInfo(localFileY: Uri, info: String, userId: String)
    fun downloadUserProfile(idClient: String)
    fun getAllUsers(idClient: String)
    fun startMessaging(opponentId: String, userId: String): Pair<DocumentReference, SnapshotStateList<Message>>?
    fun sendMessage(message: String, dialogId: DocumentReference, userId: String)
//    fun getAllMessages(userId: String, dialogId: DocumentReference)
}