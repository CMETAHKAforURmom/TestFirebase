package ru.test.andernam.view.components.screens.sendMessage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.test.andernam.view.theme.Pink80
import ru.test.andernam.view.theme.Purple80
import ru.test.andernam.view.theme.PurpleGrey80

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
            items(sendMessageViewModel.currDialogHref?.size ?: 0, itemContent = {
                Log.i("All dialog is...", sendMessageViewModel.storage.savedMessagesSnapshot[sendMessageViewModel.storage.currentDialogHref.value]?.get(it)?.messageText.toString())
                sendMessageViewModel.currDialogHref?.sortBy {sorting -> sorting.date }
                val localDateTime = sendMessageViewModel.currDialogHref?.get(it)?.date?.split(" ")?.get(1)?.split(":")
                Message(
                    messageText = sendMessageViewModel.currDialogHref?.get(it)?.messageText ?: "",
                    messageDate = (localDateTime?.get(0) ?: "") + ":" + (localDateTime?.get(1) ?: ""),
                    Modifier.padding(5.dp),
                    (sendMessageViewModel.currDialogHref?.get(it)?.user == sendMessageViewModel.storage.userPhone)
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