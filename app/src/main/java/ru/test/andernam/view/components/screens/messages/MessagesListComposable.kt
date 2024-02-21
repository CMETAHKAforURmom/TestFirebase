package ru.test.andernam.view.components.screens.messages

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.test.andernam.AppModule.provideMessageImpl
//import ru.test.andernam.AppModule.provideMessageViewModel
import ru.test.andernam.R
import ru.test.andernam.data.getDialogId

@Composable
fun BlogComp(
    action: (href: String) -> Unit,
    messageListViewModel: MessageListViewModel = MessageListViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.width(30.dp))
        Log.i("DB state messages", messageListViewModel.storage.localUsersMessagingInfo.toString())
        LazyColumn(modifier = Modifier
            .padding(15.dp)
            .align(Alignment.TopCenter)) {
            items(messageListViewModel.storage.localUsersMessagingInfo.size, itemContent = {
                CardElementUser(
//                    messageListViewModel.storage.localUsersMessagingInfo[it].dialogsList,
                    getDialogId(messageListViewModel.storage.localUsersMessagingInfo[it],
                        messageListViewModel.storage.localUserInfo.userId.value),
                    messageListViewModel.storage.localUsersMessagingInfo[it].userName.value,
                    messageListViewModel.storage.localUsersMessagingInfo[it].userImageHref.value,
                    action
                )
            })
        }
    }
}


@Composable
fun CardElementUser(
    profileLinkL: String,
    name: String,
    profileImg: Uri?,
    action: (href: String) -> Unit
) {
    val message = LocalContext.current.getString(R.string.message_screen)
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(35f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = profileImg, contentDescription = "Image profile",
            modifier = Modifier
                .padding(15.dp)
                .size(64.dp)
                .clip(CircleShape)
        )

        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = name, modifier = Modifier.padding(15.dp))
            Icon(
                Icons.Default.Send,
                contentDescription = "Send",
                modifier = Modifier.clickable {
                    action(profileLinkL)
//                    provideMessageImpl().messageHref = profileLinkL
//                    navController.navigate(provideMessageImpl().messageHref)

                }

//                    navigateTo(message)
//                    userClass.startMessagingWith(profileLinkL)
//                    setOpponentData(profileImg, name)
            )
        }
    }
}
