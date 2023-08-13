package ru.test.andernam

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import ru.test.andernam.domain.startDownload
import ru.test.andernam.domain.stillDoing
import ru.test.andernam.domain.userData
import java.util.UUID
import kotlin.concurrent.thread


lateinit var dbState: Task<DocumentSnapshot>
var iDoIt = true
var docExist = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComp(navController: NavController) {

    var loadDefault = "Still loading, please wait"

    var coroutineScope = rememberCoroutineScope()

    val mapIfDocNotExist: Map<String, String> = mapOf("clientData" to " ", "profilePhoto" to " ")

    var allDataAboutMap: Map<String?, Any> by remember {
        mutableMapOf()
    }

    var localUri by remember {
        mutableStateOf(Uri.EMPTY)
    }

    var localName by remember {
        mutableStateOf(loadDefault)
    }

    var localProfilePhotoUri  by remember {
        mutableStateOf(Uri.EMPTY)
    }

    val result = coroutineScope.async {
        startDownload(user)
    }

    while(!stillDoing){
            if (userData.second != null) {
                var (Uri, Name) = userData
                Log.i("isComplete_Main", "true")
                localUri = Uri
                localName = Name!!
            }else
                Log.i("UserDataIsNull", "It null")
        }

    var launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            localUri = uri
        }
    }


    auth.addAuthStateListener { listener ->
        if(listener.currentUser == null){
            navController.navigate(Routes.Enter.route)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(horizontal = 15.dp), verticalArrangement = Arrangement.SpaceEvenly
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(if(localUri == Uri.EMPTY) localProfilePhotoUri else localUri)
                    .build(),
                contentDescription = "Translated description of what the image contains",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            TextField(
                value = localName,
                onValueChange = { localName = it },
                Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxHeight(0.2f)
            )

            Button(
                onClick = {
//                    imageRef = storage.reference.child(localUri.toString())
//                    var newName = UUID.randomUUID().toString()
//                    var imageCloudReference = storage.getReference("$idClient/$newName")
//                    var uploadTask = imageCloudReference.putFile(localUri).addOnSuccessListener {
//                        imageCloudReference.downloadUrl.addOnSuccessListener { result ->
//                            database.collection("usersData").document(idClient)
//                                .update("profilePhoto", result)
//                            Log.i("PHOTO is: ", result.toString())
//                        }
//                    }
//                    database.collection("usersData").document(idClient).update("clientData", name)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f)
            ) {
                Text(text = "Save")
            }
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f),
                onClick = {
                    auth.signOut()
                    user = null
                    iDoIt = true
                    docExist = false
                }
            ){
                Text(text = "Sign out")
            }
        }

    }
}