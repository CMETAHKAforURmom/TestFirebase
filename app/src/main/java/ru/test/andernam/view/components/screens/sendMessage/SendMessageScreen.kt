package ru.test.andernam.view.components.screens.sendMessage

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.test.andernam.view.theme.Pink80
import ru.test.andernam.view.theme.Purple40
import ru.test.andernam.view.theme.Purple80
import ru.test.andernam.view.theme.PurpleGrey80

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SendMessageScreen(
    sendMessageViewModel: SendMessageViewModel
) {
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var previousDialogSize by remember{
        mutableStateOf(0)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Pink80)
    ) {

        LazyColumn(modifier = Modifier.padding(15.dp), state = lazyColumnState) {
            items(sendMessageViewModel.currDialogHref?.size ?: 0, itemContent = {
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
        AnimatedVisibility(visible = canScrollDown, modifier = Modifier
            .align(Alignment.BottomEnd)
            .background(Color.Transparent)
            .padding(7.dp)) {
            Button(modifier = Modifier.background(shape = CircleShape, color = Purple40).sizeIn(60.dp), onClick = {
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