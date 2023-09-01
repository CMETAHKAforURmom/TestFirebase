package ru.test.andernam.view.ui_parts

import android.net.Uri
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.test.andernam.domain.signOut
import ru.test.andernam.domain.uploadInfo


private var localPair: MutableState<Pair<Uri?, String?>> = mutableStateOf( Pair(Uri.EMPTY, null))

fun setPair(userDataPair : Pair<Uri?, String?>){
    localPair.value = userDataPair
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComp() {

    val loadDefault = "Still loading, please wait"

    var uriForUpload by remember {
        mutableStateOf(Uri.EMPTY)
    }

    var localUri by remember {
        mutableStateOf(Uri.EMPTY)
    }

    var localName by remember {
        mutableStateOf(loadDefault)
    }

    val localProfilePhotoUri  by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var isPairUpdated by remember {
        mutableStateOf(true)
    }

    if(isPairUpdated) {
        if (localPair.value.second != null) {
            isPairUpdated = false
            val (Uri, Name) = localPair.value
            localUri = Uri
            localName = Name!!
        }
    }


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            localUri = uri
            uriForUpload = uri
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            Modifier
                .fillMaxSize()
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
                    .fillMaxHeight(0.2f),
                placeholder = { Text(text = "Enter your name here")},

            )

            Button(
                onClick = {
                    if(uriForUpload != null)
                          uploadInfo(uriForUpload, localName)
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
                    signOut()
                    isPairUpdated = true
                }
            ){
                Text(text = "Sign out")
            }
        }

    }
}