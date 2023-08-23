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
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.test.andernam.view.ui_parts.setPair
import java.util.UUID

lateinit var database: FirebaseFirestore
lateinit var storage: FirebaseStorage
var docExist = false
lateinit var dbState: Task<DocumentSnapshot>
private var idClient: String? = null

fun setClient(number: String){
    idClient = number!!
}

fun uploadInfo(localFileY: Uri, info: String) = runBlocking{
    async {
        if (idClient != null) {
            var newName = UUID.randomUUID().toString()
            var imageCloudReference = storage.getReference().child("$idClient/$newName")
            var uploadTask = imageCloudReference.putFile(localFileY).addOnSuccessListener {
                imageCloudReference.downloadUrl.addOnSuccessListener { result ->
                    database.collection("usersData").document(idClient!!)
                        .update("profilePhoto", result)
                    Log.i("PHOTO is: ", result.toString())
                }
            }
                .addOnFailureListener { exception ->
                    Log.i("Exception is:", exception.toString())
                }
            database.collection("usersData").document(idClient!!).update("clientData", info)
        }
    }
}

fun startDownload() = runBlocking{
    async {
        database = Firebase.firestore
        storage = Firebase.storage
        var userData: Pair<Uri?, String?> = Pair(null, null)

        val mapIfDocNotExist: Map<String, String> =
            mapOf("clientData" to " ", "profilePhoto" to " ")

        var allDataAboutMap: Map<String?, Any> = emptyMap()

        var profilePhotoUri = Uri.EMPTY

        var profilePhotoPath: String? = null

        var name = "Some Name"

        if (idClient != null) {

            dbState = database.collection("usersData").document(idClient!!).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        allDataAboutMap = document.data!!
                        docExist = true
                    } else {
                        database.collection("usersData").document(idClient!!)
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

                    name = allDataAboutMap["clientData"].toString()
                    profilePhotoPath = allDataAboutMap["profilePhoto"].toString()
                    if (profilePhotoPath != null)
                        profilePhotoUri = Uri.parse(profilePhotoPath!!)
                    userData = Pair(profilePhotoUri, name)
                    setPair(userData)
                }
        }
    }

}
