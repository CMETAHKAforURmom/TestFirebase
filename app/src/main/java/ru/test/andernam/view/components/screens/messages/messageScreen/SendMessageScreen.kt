package ru.test.andernam.view.components.screens.messages.messageScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.test.andernam.view.theme.Pink40
import ru.test.andernam.view.theme.Pink80
import ru.test.andernam.view.theme.Purple40
import ru.test.andernam.view.theme.Purple80
import ru.test.andernam.view.theme.PurpleGrey80

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SendMessageScreen(
    sendMessageViewModel: SendMessageViewModel,
    onBack: () -> Unit
) {
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var opponentUser = sendMessageViewModel.opponentUser
    var previousDialogSize by remember{
        mutableIntStateOf(0)
    }
    var message by remember {
        mutableStateOf("")
    }

    var canScrollDown by remember{
        mutableStateOf(false)
    }

    canScrollDown = lazyColumnState.canScrollForward

    if(previousDialogSize != sendMessageViewModel.currDialogHref?.size && sendMessageViewModel.currDialogHref?.size != 0){
        coroutineScope.launch {
            lazyColumnState.animateScrollToItem(sendMessageViewModel.currDialogHref?.size!!)
        }
        previousDialogSize = sendMessageViewModel.currDialogHref?.size!!
    }

    Column (Modifier.fillMaxSize().imePadding()) {
        Spacer(Modifier.height(48.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .size(64.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(18.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(32.dp).clickable {
                    onBack.invoke()
                })
            AsyncImage(
                model = opponentUser.userImageHref.value, contentDescription = "Image profile",
                modifier = Modifier
                    .padding(15.dp)
                    .size(54.dp)
                    .clip(CircleShape)
            )
            Text(text = opponentUser.userName.value)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Pink80)
        ) {
            LazyColumn(modifier = Modifier.padding(15.dp), state = lazyColumnState) {
                items(sendMessageViewModel.currDialogHref?.size ?: 0, itemContent = {
                    sendMessageViewModel.currDialogHref?.sortBy { sorting -> sorting.date }
                    val localDateTime =
                        sendMessageViewModel.currDialogHref?.get(it)?.date?.split(" ")?.get(1)
                            ?.split(":")
                    Message(
                        messageText = sendMessageViewModel.currDialogHref?.get(it)?.messageText
                            ?: "",
                        messageDate = (localDateTime?.get(0) ?: "") + ":" + (localDateTime?.get(1)
                            ?: ""),
                        Modifier.padding(5.dp),
                        (sendMessageViewModel.currDialogHref?.get(it)?.user == sendMessageViewModel.storage.userUID)
                    )
                })
            }

            //WTF?
            this@Column.AnimatedVisibility(
                visible = canScrollDown, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Transparent)
                    .padding(7.dp)
            ) {
                Button(
                    modifier = Modifier.background(shape = CircleShape, color = Purple40)
                        .sizeIn(60.dp), onClick = {
                        coroutineScope.launch {
                            lazyColumnState.animateScrollToItem(sendMessageViewModel.currDialogHref?.size!!)
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Move to down"
                    )
                }
            }
        }
            Row(
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxWidth()
                    .imePadding()
                    .background(Purple40),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.width(1.dp))
                OutlinedTextField(
                    value = message, onValueChange = { message = it }, modifier = Modifier
                        .fillMaxWidth(0.8f)
//                        .border(1.dp, Color.DarkGray, RoundedCornerShape(35f))
                )
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send action",
                    modifier = Modifier
                        .clickable {
                            sendMessageViewModel.sendMessage(message)
                            message = ""
                        }
                        .size(42.dp))
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
                    .padding(bottom = 10.dp, end = 3.dp)
            )
            Text(
                text = messageDate, fontSize = 10.sp, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
            )

        }
    }
}