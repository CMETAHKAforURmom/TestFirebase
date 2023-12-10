package ru.test.andernam.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import ru.test.andernam.domain.ipl.DatabaseRequests

class UserClass {

    var DBRequest: DatabaseRequests? = null
    var user: FirebaseUser? = null
    var idClient: String? = null
    var auth: FirebaseAuth? = null
    var currentDialog: DocumentReference? = null
    var dialogList: SnapshotStateList<Message>? = null
    var phoneAuthStr: String? = null
    var isAuth: Boolean = false
}