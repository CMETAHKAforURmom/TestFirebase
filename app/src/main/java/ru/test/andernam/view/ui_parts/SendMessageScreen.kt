package ru.test.andernam.view.ui_parts

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.test.andernam.domain.Message
import ru.test.andernam.domain.allMessageForPost
import ru.test.andernam.domain.getAllMesages
import ru.test.andernam.view.ui_parts.Scaffold.setOpponentData

var messageGettingList: SnapshotStateList<Message> = mutableStateListOf()
var thisUser = ""
var opponentUserImage = mutableStateOf(Uri.EMPTY)
var opponentUserName = mutableStateOf(" ")

fun setMessagePathAndUsers(messageListPath: SnapshotStateList<Message>, Client: String) {
    messageGettingList = messageListPath
    thisUser = Client
}

fun setOpponentData(imageUri: Uri, opponentName: String){
    opponentUserImage.value = imageUri
    opponentUserName.value = opponentName
}

@Composable
fun SendMessageScreen() {
    getAllMesages()
    setOpponentData(opponentUserImage, opponentUserName)
    LazyColumn(modifier = Modifier.padding(15.dp)) {
        items(messageGettingList.size, itemContent = {
            message(
                messageText = messageGettingList[it].MessageText,
                Modifier.padding(5.dp),
                (messageGettingList[it].User == thisUser)
            )
        })
    }
}

@Composable
fun message(messageText: String, modifier: Modifier = Modifier, flagAddress: Boolean) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .border(1.dp, Color.DarkGray, RoundedCornerShape(45f))
                .align(if (flagAddress) Alignment.CenterEnd else Alignment.CenterStart)
        ) {
            Text(
                text = messageText,
                Modifier
                    .align(Alignment.Center)
                    .padding(7.dp)
            )
        }
    }
}