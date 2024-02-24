package ru.test.andernam.view.components.screens.profile

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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

@Composable
fun MainComp(profileViewModel: ProfileViewModel,
             navigateToEnter: () -> Unit) {

    var uriForUpload by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var localUri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    val localProfilePhotoUri = profileViewModel.storage.localUserInfo.userImageHref
    val userName = profileViewModel.storage.localUserInfo.userName

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
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
                    .data(localProfilePhotoUri.value)
                    .build(),
                contentDescription = "Your avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            TextField(
                value = userName.value,
                onValueChange = { userName.value = it },
                Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxHeight(0.2f),
                placeholder = { Text(text = "Enter your name here") },

                )

            Button(
                onClick = {
                    profileViewModel.saveUserData(uriForUpload, userName.value)
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
                    profileViewModel.exitAccount()
                    navigateToEnter.invoke()
                }
            ) {
                Text(text = "Sign out")
            }
        }

    }
}
