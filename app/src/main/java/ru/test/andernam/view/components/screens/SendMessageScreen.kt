package ru.test.andernam.view.components.screens

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.test.andernam.domain.Message
import ru.test.andernam.ui.theme.Pink80
import ru.test.andernam.ui.theme.Purple80
import ru.test.andernam.ui.theme.PurpleGrey80
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SendMessageScreen() {
//    getAllMessages() // Call "downloadPrewMessages" from DownloadUploadHelp and get snapshotState
    setOpponentData(opponentUserImage, opponentUserName)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Pink80)){
    LazyColumn(modifier = Modifier.padding(15.dp)) {
        items(messageGettingList.size, itemContent = {
            val localDateTime = messageGettingList[it].date.split(" ")[1].split(":")
            Message(
                messageText = messageGettingList[it].messageText,
                messageDate = localDateTime[0]+ ":" + localDateTime[1],
                Modifier.padding(5.dp),
                (messageGettingList[it].user == thisUser)
            )
        })
    }
    }
}

@Composable
fun Message(messageText: String, messageDate: String, modifier: Modifier = Modifier, flagAddress: Boolean) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .border(1.dp, Color.DarkGray, RoundedCornerShape(45f))
                .align(if (flagAddress) Alignment.CenterEnd else Alignment.CenterStart)
                .drawBehind {
                    drawRoundRect(
                        if (flagAddress) Purple80 else PurpleGrey80,
                        cornerRadius = CornerRadius(45f)
                    )
                }

        ) {
            Text(
                text = messageText,
                Modifier
                    .align(Alignment.Center)
                    .padding(7.dp)
                    .padding(bottom = 7.dp, end = 3.dp)
            )
            Text(text = messageDate, fontSize = 10.sp, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(5.dp))

        }
    }

}