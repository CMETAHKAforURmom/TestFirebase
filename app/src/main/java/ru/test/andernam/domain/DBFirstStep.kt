package ru.test.andernam.domain

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.test.andernam.domain.interfaces.DBFirstStepIpl
import ru.test.andernam.domain.repository.LiveUserData
import ru.test.andernam.domain.repository.ProfileInfo
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBFirstStep @Inject constructor() : DBFirstStepIpl {

    override lateinit var idUser: String
    override lateinit var idOpponent: String
    override var database: FirebaseFirestore = Firebase.firestore
    override var storage: FirebaseStorage = Firebase.storage
    override lateinit var userInfo: ProfileInfo

    @Inject
    override lateinit var liveUserData: LiveUserData

    override fun downloadUserProfile(idClient: String, thisUser: Boolean): Unit = runBlocking {

        if (thisUser)
            idUser = idClient
        async {
            val mapIfDocNotExist: Map<String, String> =
                mapOf("clientData" to " ", "profilePhoto" to " ", "dialogs" to "")

            var allDataAboutMap: Map<String?, Any> = emptyMap()

            database.collection("usersData").document(idClient).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        allDataAboutMap = document.data!!
                    } else {
                        database.collection("usersData").document(idClient)
                            .set(mapIfDocNotExist)
                            .addOnSuccessListener {
                                Log.i("Creating new doc", "Success!")
                            }.addOnFailureListener {
                                Log.i("Creating new doc", "No success!")
                            }
                    }

                }
                .addOnFailureListener {
                }
                .addOnCompleteListener {

                    userInfo = ProfileInfo(
                        name = allDataAboutMap["clientData"].toString(),
                        linkImage = Uri.parse(allDataAboutMap["profilePhoto"].toString()),
                        dialogsLink = allDataAboutMap["dialogs"].toString()
                    )

                }

        }
    }

    override fun uploadInfo(localFileY: Uri, info: String): Unit = runBlocking { //Use Dagger!
        async {
            val newName = UUID.randomUUID().toString()
            val imageCloudReference = storage.reference.child("$idUser/$newName")
            imageCloudReference.putFile(localFileY).addOnSuccessListener {
                imageCloudReference.downloadUrl.addOnSuccessListener { result ->
                    database.collection("usersData").document(idUser)
                        .update("profilePhoto", result)
                }
            }
                .addOnFailureListener { _ ->
                }
            database.collection("usersData").document(idUser).update("clientData", info)

        }
    }
}
