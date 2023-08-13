package ru.test.andernam.domain

import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ru.test.andernam.contextActivity
import ru.test.andernam.database
import ru.test.andernam.dbState
import ru.test.andernam.docExist
import ru.test.andernam.iDoIt
import ru.test.andernam.storage
import ru.test.andernam.user
import kotlin.coroutines.CoroutineContext


var userData: Pair<Uri?, String?> = Pair(null, null)
var stillDoing = true

fun startDownload(user: FirebaseUser?){

    var loadDefault = "Still loading, please wait"

    val mapIfDocNotExist: Map<String, String> = mapOf("clientData" to " ", "profilePhoto" to " ")

    var allDataAboutMap: Map<String?, Any> = emptyMap()

    var profilePhotoUri = Uri.EMPTY

    var profilePhotoPath: String? = null

    var idClient: String? = null

    if (user != null)
        if (user.phoneNumber != null)
            idClient = user.phoneNumber!!

    var name = "Some Name"

    if (idClient != null) {

        dbState = database.collection("usersData").document(idClient).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    allDataAboutMap = document.data!!
                    docExist = true
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

                    name = allDataAboutMap["clientData"].toString()
                    profilePhotoPath = allDataAboutMap["profilePhoto"].toString()
                if (profilePhotoPath!!.contains("//")) {
                    storage.getReferenceFromUrl(profilePhotoPath!!).downloadUrl.addOnSuccessListener { result ->
                        profilePhotoUri = result
                    }
                } else
                    profilePhotoUri =
                        Uri.parse("https://mriyaresort.com/local/templates/.default/assets/img/loader/loading-thumb.gif")
                userData = Pair(profilePhotoUri, name)
                stillDoing = false
            }
    }

}
