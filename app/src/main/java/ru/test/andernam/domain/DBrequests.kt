package ru.test.andernam.domain

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import ru.test.andernam.view.ui_parts.setMessagePathAndUsers
import ru.test.andernam.view.ui_parts.setPair
import ru.test.andernam.view.ui_parts.setUsers
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID
import kotlin.coroutines.coroutineContext

lateinit var database: FirebaseFirestore
lateinit var storage: FirebaseStorage
lateinit var messageLink: DocumentReference
lateinit var userListSnapshot: Task<QuerySnapshot>
var yourOpponentNow = " "
var yourOpponentImage = Uri.EMPTY
var allMessages: SnapshotStateList<Message> = mutableStateListOf(Message("You", "Some Message"))
var allMessagesPost: SnapshotStateList<Message> = mutableStateListOf(Message("You", "Some Message"))
var allMessageForPost: SnapshotStateList<Message> = mutableStateListOf(Message("You", "Some Message"))
var available = mutableStateOf(false)
var newMessage = mutableStateOf(false)
var docExist = false
lateinit var clientDialogsList: String
lateinit var dbState: Task<DocumentSnapshot>
private var idClient: String? = null

fun setClient(number: String) {
    idClient = number!!
}

fun uploadInfo(localFileY: Uri, info: String) = runBlocking {
    async {
        if (idClient != null) {
            var newName = UUID.randomUUID().toString()
            var imageCloudReference = storage.getReference().child("$idClient/$newName")
            var uploadTask = imageCloudReference.putFile(localFileY).addOnSuccessListener {
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

fun startDownload() = runBlocking {
    async {
        database = Firebase.firestore
        storage = Firebase.storage
        var userData: Pair<Uri?, String?> = Pair(null, null)

        val mapIfDocNotExist: Map<String, String> =
            mapOf("clientData" to " ", "profilePhoto" to " ", "dialogs" to "")

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

fun getAllUsers() {
    var usersDataArray = mutableListOf<Array<String>>()
    var indexHelp = 0
    userListSnapshot = database.collection("usersData").get().addOnSuccessListener { result ->
        result.forEach { document ->

            var localUsers = arrayOf(
                document.id,
                document.data!!["clientData"].toString(),
                document.data!!["profilePhoto"].toString()
            )
            if (document.data!!["clientData"] != null)
                usersDataArray.add(localUsers)
            indexHelp++
        }
    }.addOnCompleteListener {
        setUsers(usersDataArray)
    }
}

fun startMessaging(userId: String) {

    var messagePath = ""

    if (clientDialogsList.contains(userId)) {
        var listExist = clientDialogsList.split(",")
        var userIdRandom = listExist.filter { it.contains(userId) }
        var currentString = userIdRandom[0]
        userIdRandom = currentString.split("|")
        messagePath = userIdRandom[1]
    } else {
        messagePath = UUID.randomUUID().toString()
        val mapIfDocNotExist: Map<String, String> =
            mapOf("firstRandomMessage" to " ")
        database.collection("dialogs").document(messagePath)
            .set(mapIfDocNotExist).addOnCompleteListener {
                clientDialogsList += ",$userId|$messagePath"
                database.collection("usersData").document(idClient!!)
                    .update("dialogs", clientDialogsList)
                var opponentDialogList = ",$idClient|$messagePath"
                database.collection("usersData").document(userId)
                    .update("dialogs", opponentDialogList)
            }
    }
    messageLink = database.collection("dialogs").document(messagePath)

}

fun sendMessage(message: String) = runBlocking {
    val sdf = SimpleDateFormat("dd,M,yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())
    if (messageLink != null)
        messageLink.update("$currentDate|$idClient", message)
    else
        Log.i("MessageLinkEmpty", "RealEmpty")
}

fun getAllMesages() = runBlocking {
    setMessagePathAndUsers(allMessageForPost, idClient!!)
    var messageHelperMap: MutableMap<String?, Any?> = mutableMapOf()

    messageLink.addSnapshotListener { snapshot, error ->
        messageHelperMap = snapshot!!.data!!
        messageHelperMap.forEach { it ->
            var anyString = it.key?.split("|")
            allMessages.add(Message(anyString!![1], it.value.toString()))
        }
        allMessages = allMessages.minus(allMessagesPost).toMutableStateList()
        allMessagesPost = allMessagesPost.plus(allMessages).toMutableStateList()
        allMessageForPost += allMessages
    }
}
