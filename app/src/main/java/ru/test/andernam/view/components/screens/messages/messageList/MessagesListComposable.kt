package ru.test.andernam.view.components.screens.messages.messageList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.test.andernam.data.UserInfo

@Composable
fun BlogComp(
    actionToGo: () -> Unit,
    messageListViewModel: MessageListViewModel,
    searchNavigate: () -> Unit
) {
    val recentUsers = messageListViewModel.recentUsers.keys.toMutableList()

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
//        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(48.dp).clickable {
                    searchNavigate.invoke()
                })
        }
        Spacer(modifier = Modifier.width(24.dp))
        LazyColumn(
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            items(recentUsers.size, itemContent = {
                CardElementUser(
                    recentUsers[it],
                    actionToGo = { dialogHref ->
                        actionToGo.invoke()
                        messageListViewModel.selectDialog(dialogHref)
                    },
                    methodGetHref = { opponentUser ->
                        messageListViewModel.startMessaging(opponentUser)
                    })
            })
        }
    }
}


//  ELEMENTS CLASS!!!
@Composable
fun CardElementUser(
    opponentUser: UserInfo,
    actionToGo: (String) -> Unit,
    methodGetHref: suspend (UserInfo) -> String
) {
    var dialogHref by remember {
        mutableStateOf("")
    }
    val coroutine = remember {
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    }
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(20.dp))
            .clickable {
                coroutine.launch {
                    dialogHref = methodGetHref(opponentUser)
                    actionToGo(dialogHref)
                }
            }
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp)),
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
//            Icon(
//                Icons.Default.Send,
//                contentDescription = "Send",
//                modifier = Modifier.clickable {
//                    coroutine.launch {
//                        dialogHref = methodGetHref(opponentUser)
//                        actionToGo(dialogHref)
//                    }
//                }
//            )
        }
    }
}
