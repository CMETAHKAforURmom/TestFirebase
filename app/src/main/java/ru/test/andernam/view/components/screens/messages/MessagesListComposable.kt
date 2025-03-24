package ru.test.andernam.view.components.screens.messages

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.test.andernam.data.UserInfo

@Composable
fun BlogComp(
    actionToGo: () -> Unit,
    messageListViewModel: MessageListViewModel
) {
    val recentUsers = messageListViewModel.recentUsers

    Log.i("View Messages", "$recentUsers")

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.width(30.dp))
        LazyColumn(
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.TopCenter)
        ) {
            items(recentUsers.size, itemContent = {
                if (recentUsers[it] != null)
                CardElementUser(
                    recentUsers[it]!!,
                    actionToGo = { dialogHref ->
                    actionToGo.invoke()
                    messageListViewModel.selectDialog(dialogHref)
                }, methodGetHref = {opponentUser -> messageListViewModel.startMessaging(opponentUser)})
            })
        }
    }
}


@Composable
fun CardElementUser(
    opponentUser: UserInfo,
    actionToGo: (String) -> Unit,
    methodGetHref: suspend (UserInfo) -> String
) {
    var dialogHref by remember {
        mutableStateOf("")
    }
    val coroutine = rememberCoroutineScope()
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(35f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = opponentUser.userImageHref.value, contentDescription = "Image profile",
            modifier = Modifier
                .padding(15.dp)
                .size(64.dp)
                .clip(CircleShape)
        )

        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = opponentUser.userName.value, modifier = Modifier.padding(15.dp))
            Icon(
                Icons.Default.Send,
                contentDescription = "Send",
                modifier = Modifier.clickable {
                    coroutine.launch {
                        dialogHref = methodGetHref(opponentUser)
                        actionToGo(dialogHref)
                    }
                }
            )
        }
    }
}
