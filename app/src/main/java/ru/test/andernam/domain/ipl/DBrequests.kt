package ru.test.andernam.domain.ipl

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ru.test.andernam.domain.Message

interface DatabaseRequests {

    var database: FirebaseFirestore
    var storage: FirebaseStorage
    var messageLink: DocumentReference
    var docExist: Boolean
    var clientDialogsList: String
    var dbState: Task<DocumentSnapshot>

    fun uploadInfo(localFileY: Uri, info: String, userId: String)
    fun downloadUserProfile(userId: String)
    fun getAllUsers()
    fun startMessaging(opponentId: String, userId: String): Any
    fun sendMessage(message: String, dialogId: DocumentReference, userId: String)
    fun getAllMessages(userId: String, dialogId: DocumentReference)
}
