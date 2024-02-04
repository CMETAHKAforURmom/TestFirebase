package ru.test.andernam.domain.interfaces

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.test.andernam.domain.repository.ProfileInfo
import ru.test.andernam.domain.repository.LiveUserData
import java.util.UUID

interface DBFirstStepIpl {

    var idUser: String
    var idOpponent: String
    var database: FirebaseFirestore
    var storage: FirebaseStorage
    var userInfo: ProfileInfo
    var liveUserData: LiveUserData
    fun downloadUserProfile(idClient: String, thisUser: Boolean)

    fun uploadInfo(localFileY: Uri, info: String)
}