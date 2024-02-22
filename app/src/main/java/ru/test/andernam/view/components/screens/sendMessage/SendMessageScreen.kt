package ru.test.andernam.view.components.screens.sendMessage

import android.net.Uri
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
import ru.test.andernam.domain.old.Message
import ru.test.andernam.view.theme.Pink80
import ru.test.andernam.view.theme.Purple80
import ru.test.andernam.view.theme.PurpleGrey80

var messageGettingList: SnapshotStateList<Message> = mutableStateListOf()
var thisUser = ""
var opponentUserImage = mutableStateOf(Uri.EMPTY)
var opponentUserName = mutableStateOf(" ")

fun setMessagePathAndUsers(messageListPath: SnapshotStateList<Message>, Client: String) {
    messageGettingList = messageListPath
    thisUser = Client
}

fun setOpponentData(imageUri: Uri, opponentName: String) {
    opponentUserImage.value = imageUri
    opponentUserName.value = opponentName
}

@Composable
fun SendMessageScreen(
    sendMessageViewModel: SendMessageViewModel
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Pink80)
    ) {
        LazyColumn(modifier = Modifier.padding(15.dp)) {
            items(sendMessageViewModel.dialog?.size ?: 0, itemContent = {
                val localDateTime = sendMessageViewModel.dialog?.get(it)?.date?.split(" ")?.get(1)?.split(":")
                Message(
                    messageText = sendMessageViewModel.dialog?.get(it)?.messageText ?: "",
                    messageDate = (localDateTime?.get(0) ?: "") + ":" + (localDateTime?.get(1) ?: ""),
                    Modifier.padding(5.dp),
                    (sendMessageViewModel.dialog?.get(it)?.user == thisUser)
                )
            })
        }
    }
}

@Composable
fun Message(
    messageText: String,
    messageDate: String,
    modifier: Modifier = Modifier,
    flagAddress: Boolean
) {
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
            Text(
                text = messageDate, fontSize = 10.sp, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
            )

        }
    }
}