//package ru.test.andernam.old.interfaces.old
//
//import android.annotation.SuppressLint
//import android.net.Uri
//import android.util.Log
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.runtime.toMutableStateList
//import com.google.android.gms.tasks.Task
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.ktx.storage
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.async
//import kotlinx.coroutines.runBlocking
//import ru.test.andernam.old.interfaces.old.ipl.DatabaseRequests
//import ru.test.andernam.old.interfaces.repository.LiveUserData
//import ru.test.andernam.view.components.screens.sendMessage.setMessagePathAndUsers
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.UUID
//import javax.inject.Inject
//
//@Module
//@InstallIn(SingletonComponent::class)
//class DataBaseRequestImpl @Inject constructor(private val liveUserData: LiveUserData):
//    DatabaseRequests {
//
//    override lateinit var database: FirebaseFirestore
//    override lateinit var storage: FirebaseStorage
//    override lateinit var messageLink: DocumentReference
//    override var docExist = false
//    override lateinit var clientDialogsList: String
//    override lateinit var dbState: Task<DocumentSnapshot>
//
//    override fun uploadInfo(localFileY: Uri, info: String, userId: String): Unit = runBlocking { //Use Dagger!
//        async {
//                val newName = UUID.randomUUID().toString()
//                val imageCloudReference = storage.reference.child("$userId/$newName")
//                imageCloudReference.putFile(localFileY).addOnSuccessListener {
//                    imageCloudReference.downloadUrl.addOnSuccessListener { result ->
//                        database.collection("usersData").document(userId)
//                            .update("profilePhoto", result)
//                    }
//                }
//                    .addOnFailureListener { _ ->
//                    }
//                database.collection("usersData").document(userId).update("clientData", info)
//
//        }
//    }
//
////    fun getUserProfile(idClient: String) : LiveData<UserClass>{
////        var userClass = UserClass()
////
////    }
//
//    override fun downloadUserProfile(idClient: String) {
////        async {
//            database = Firebase.firestore
//            storage = Firebase.storage
//
//            var userData: Pair<Uri?, String?>
//
//            val mapIfDocNotExist: Map<String, String> =
//                mapOf("clientData" to " ", "profilePhoto" to " ", "dialogs" to "")
//
//            var allDataAboutMap: Map<String?, Any> = emptyMap()
//
//            var profilePhotoUri = Uri.EMPTY
//
//            var profilePhotoPath: String?
//
//            var name: String
//
//            var userDataClass = UserClass()
//
//                dbState = database.collection("usersData").document(idClient).get()
//                    .addOnSuccessListener { document ->
//                        if (document.exists()) {
//                            allDataAboutMap = document.data!!
//                            docExist = true
//                        } else {
//                            database.collection("usersData").document(idClient)
//                                .set(mapIfDocNotExist)
//                                .addOnSuccessListener {
//                                    Log.i("Creating new doc", "Success!")
//                                }.addOnFailureListener {
//                                    Log.i("Creating new doc", "No success!")
//                                }
//                        }
//
//                    }
//                    .addOnFailureListener {
//                    }
//                    .addOnCompleteListener {
//
//                        name = allDataAboutMap["clientData"].toString()
//                        profilePhotoPath = allDataAboutMap["profilePhoto"].toString()
//                        clientDialogsList = allDataAboutMap["dialogs"].toString()
//
////
////                        println(name)
////                        var userClass = ProfileInfo()
////                        userClass.name = name
////                        userClass.linkImage = Uri.parse(profilePhotoPath)
////                        userProfileInfo.linkImage = profilePhotoPath
////                        userProfileInfo.dialogsLink = clientDialogsList
//
////                        if (profilePhotoPath != null)
////                            userProfileInfo.linkImage = Uri.parse(profilePhotoPath!!)
////                            profilePhotoUri = Uri.parse(profilePhotoPath!!)
////                        userData = Pair(profilePhotoUri, name)
////                        setPair(userData)
////                        liveUserData.profileInfo.value = userProfileInfo
////                        liveUserData.profileInfo.postValue(userClass)
////                        liveUserData.setProfileInfo(userClass)
//                    }
////        }
//
//
////        var newLiveData = MutableLiveData(userDataClass)
////        newLiveData.value = userDataClass
////            getAllUsers(idClient)
////        return newLiveData
//        }
//
//
////    }
//
//    @SuppressLint("SuspiciousIndentation")
//    override fun getAllUsers(idClient: String){
//        val usersDataArray = mutableListOf<Array<String>>()
//        var indexHelp = 0
//                val databaseTask = database.collection("usersData").get().addOnSuccessListener { result ->
//                    result.forEach { document ->
//                        if (document.data["clientData"].toString() != idClient) {
//                            val localUsers = arrayOf(
//                                document.id,
//                                document.data["clientData"].toString(),
//                                document.data["profilePhoto"].toString()
//                            )
//
//                            if (document.data["clientData"] != null)
//                                usersDataArray.add(localUsers)
//                            indexHelp++
//                        }
//                    }
//                }.addOnCompleteListener{
//                    User.dialogList = usersDataArray
//                }
//            }
//
//    @SuppressLint("SimpleDateFormat")
//    override fun startMessaging(opponentId: String, userId: String): Pair<DocumentReference, SnapshotStateList<Message>> {
//
//        val messagePath: String
//
//        if (clientDialogsList.contains(opponentId)) {
//            val listExist = clientDialogsList.split(",")
//            var userIdRandom = listExist.filter { it.contains(opponentId) }
//            val currentString = userIdRandom[0]
//            userIdRandom = currentString.split("|")
//            messagePath = userIdRandom[1]
//        } else {
//            messagePath = UUID.randomUUID().toString()
//            val mapIfDocNotExist: Map<String, String> =
//                mapOf("firstRandomMessage" to " ")
//            database.collection("dialogs").document(messagePath)
//                .set(mapIfDocNotExist).addOnCompleteListener {
//                    clientDialogsList += ",$opponentId|$messagePath"
//                    database.collection("usersData").document(userId)
//                        .update("dialogs", clientDialogsList)
//                    val opponentDialogList = ",$userId|$messagePath"
//                    database.collection("usersData").document(opponentId)
//                        .update("dialogs", opponentDialogList)
//                }
//        }
//
//        val allMessageForPost: SnapshotStateList<Message> = mutableStateListOf()
//        var localMessages: SnapshotStateList<Message> = mutableStateListOf()
//        var localHelperMessages: SnapshotStateList<Message> = mutableStateListOf()
//        setMessagePathAndUsers(allMessageForPost, userId)
//        var messageHelperMap: MutableMap<String?, Any?>
//
//        database.collection("dialogs").document(messagePath).addSnapshotListener { snapshot, _ ->
//            messageHelperMap = snapshot!!.data!!
//            messageHelperMap.forEach {
//                val anyString = it.key?.split("|")
//                localMessages.add(Message(anyString!![0], anyString[1], it.value.toString()))
//            }
//            localMessages.sortBy { SimpleDateFormat("yyyy,M,dd hh:mm:ss").parse(it.date) }
//            localMessages = localMessages.minus(localHelperMessages).toMutableStateList()
//            localHelperMessages = localHelperMessages.plus(localMessages).toMutableStateList()
//            allMessageForPost += localMessages
//        }
//
//        return Pair(database.collection("dialogs").document(messagePath), allMessageForPost)
//
//    }
//
//    @SuppressLint("SimpleDateFormat", "SuspiciousIndentation")
//    override fun sendMessage(message: String, dialogId: DocumentReference, userId: String): Unit = runBlocking {
//        val sdf = SimpleDateFormat("yyyy,M,dd hh:mm:ss")
//        val currentDate = sdf.format(Date())
//            dialogId.update("$currentDate|$userId", message)
//    }
//
////    @RequiresApi(Build.VERSION_CODES.O)
////    override fun getAllMessages(idClient: String, dialogId: DocumentReference): Unit = runBlocking {
////    }
//
//}