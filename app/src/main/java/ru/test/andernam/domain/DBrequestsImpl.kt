package ru.test.andernam.domain

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.test.andernam.domain.ipl.DatabaseRequests
import ru.test.andernam.view.ui_parts.setMessagePathAndUsers
import ru.test.andernam.view.ui_parts.setPair
import ru.test.andernam.view.ui_parts.setUsers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class DataBaseRequestImpl: DatabaseRequests {

    override lateinit var database: FirebaseFirestore
    override lateinit var storage: FirebaseStorage
    override lateinit var messageLink: DocumentReference
    override var docExist = false
    override lateinit var clientDialogsList: String
    override lateinit var dbState: Task<DocumentSnapshot>

    override fun uploadInfo(localFileY: Uri, info: String, idClient: String): Unit = runBlocking { //Use Dagger!
        async {
            if (idClient != null) {
                val newName = UUID.randomUUID().toString()
                val imageCloudReference = storage.reference.child("$idClient/$newName")
                imageCloudReference.putFile(localFileY).addOnSuccessListener {
                    imageCloudReference.downloadUrl.addOnSuccessListener { result ->
                        database.collection("usersData").document(idClient!!)
                            .update("profilePhoto", result)
                    }
                }
                    .addOnFailureListener { exception ->
                    }
                database.collection("usersData").document(idClient!!).update("clientData", info)
            }
        }
    }

    override fun downloadUserProfile(idClient: String): Unit = runBlocking {
        async {
            database = Firebase.firestore
            storage = Firebase.storage

            var userData: Pair<Uri?, String?>

            val mapIfDocNotExist: Map<String, String> =
                mapOf("clientData" to " ", "profilePhoto" to " ", "dialogs" to "")

            var allDataAboutMap: Map<String?, Any> = emptyMap()

            var profilePhotoUri = Uri.EMPTY

            var profilePhotoPath: String?

            var name: String

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
                        clientDialogsList = allDataAboutMap["dialogs"].toString()
                        if (profilePhotoPath != null)
                            profilePhotoUri = Uri.parse(profilePhotoPath!!)
                        userData = Pair(profilePhotoUri, name)
                        setPair(userData)
                    }
            }
            getAllUsers()
        }

    }

    override fun getAllUsers() {
        val usersDataArray = mutableListOf<Array<String>>()
        var indexHelp = 0
        database.collection("usersData").get().addOnSuccessListener { result ->
            result.forEach { document ->

                val localUsers = arrayOf(
                    document.id,
                    document.data["clientData"].toString(),
                    document.data["profilePhoto"].toString()
                )
                if (document.data["clientData"] != null)
                    usersDataArray.add(localUsers)
                indexHelp++
            }
        }.addOnCompleteListener {
            setUsers(usersDataArray)
        }
    }

    override fun startMessaging(opponentId: String, idClient: String): Pair<DocumentReference, SnapshotStateList<Message>> {

        var messagePath = ""

        if (clientDialogsList.contains(opponentId)) {
            val listExist = clientDialogsList.split(",")
            var userIdRandom = listExist.filter { it.contains(opponentId) }
            val currentString = userIdRandom[0]
            userIdRandom = currentString.split("|")
            messagePath = userIdRandom[1]
        } else {
            messagePath = UUID.randomUUID().toString()
            val mapIfDocNotExist: Map<String, String> =
                mapOf("firstRandomMessage" to " ")
            database.collection("dialogs").document(messagePath)
                .set(mapIfDocNotExist).addOnCompleteListener {
                    clientDialogsList += ",$opponentId|$messagePath"
                    database.collection("usersData").document(idClient!!)
                        .update("dialogs", clientDialogsList)
                    val opponentDialogList = ",$idClient|$messagePath"
                    database.collection("usersData").document(opponentId)
                        .update("dialogs", opponentDialogList)
                }
        }
        return database.collection("dialogs").document(messagePath)

    }

    override fun sendMessage(message: String, dialogId: DocumentReference, idClient: String): Unit = runBlocking {
        val sdf = SimpleDateFormat("yyyy,M,dd hh:mm:ss")
        val currentDate = sdf.format(Date())
        if (dialogId != null)
            dialogId.update("$currentDate|$idClient", message)
        else
            Log.i("MessageLinkEmpty", "RealEmpty")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllMessages(idClient: String, dialogId: DocumentReference): Unit = runBlocking {
        var allMessageForPost: SnapshotStateList<Message> = mutableStateListOf()
        setMessagePathAndUsers(allMessageForPost, idClient!!)
        var messageHelperMap: MutableMap<String?, Any?> = mutableMapOf()

        dialogId.addSnapshotListener { snapshot, error ->
            messageHelperMap = snapshot!!.data!!
            messageHelperMap.forEach {
                val anyString = it.key?.split("|")
                allMessages.add(Message(anyString!![0], anyString!![1], it.value.toString()))
            }
            allMessages.sortBy { SimpleDateFormat("yyyy,M,dd hh:mm:ss").parse(it.date) }
            allMessages = allMessages.minus(allMessagesPost).toMutableStateList()
            allMessagesPost = allMessagesPost.plus(allMessages).toMutableStateList()
            allMessageForPost += allMessages
        }

    }

}