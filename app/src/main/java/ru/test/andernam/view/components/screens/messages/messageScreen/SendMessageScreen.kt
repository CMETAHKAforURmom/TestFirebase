package ru.test.andernam.view.components.screens.messages.messageScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.test.andernam.data.Message

@Composable
fun SendMessageScreen(
    sendMessageViewModel: SendMessageViewModel,
    onBack: () -> Unit
) {
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var opponentUser = sendMessageViewModel.opponentUser
    var previousDialogSize by remember {
        mutableIntStateOf(0)
    }
    val imageModel by remember { mutableStateOf(opponentUser.userImageHref.value) }
    var message by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    var canScrollDown by remember {
        mutableStateOf(false)
    }

    val localDialogSnapshot by remember {
        derivedStateOf {
            sendMessageViewModel.currDialogHref?.sortedByDescending { it.date } ?: emptyList()
        }
    }

//    val localDialogSnapshot: List<Message> = List(30) {
//        Message(
//            List(8) { ('a'..'z').random() }.joinToString(""),
//            List(8) { ('a'..'z').random() }.joinToString(""),
//            List(8) { ('a'..'z').random() }.joinToString(""),
//            List(8) { ('a'..'z').random() }.joinToString("")
//        )
//    }

    canScrollDown = lazyColumnState.canScrollBackward

    if (previousDialogSize != sendMessageViewModel.currDialogHref?.size && sendMessageViewModel.currDialogHref?.size != 0 && !canScrollDown) {
        LaunchedEffect(coroutineScope) {
        coroutineScope.launch {
            lazyColumnState.animateScrollToItem(0)
        }
        previousDialogSize = sendMessageViewModel.currDialogHref?.size ?: 0
            }
    }
//    else Button +1

    Column(Modifier
        .fillMaxSize()
        .imePadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .size(64.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(18.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onBack.invoke()
                    })
            AsyncImage(
                model = imageModel,
                contentDescription = "Image profile",
                modifier = Modifier
                    .padding(15.dp)
                    .size(54.dp)
                    .clip(CircleShape),
                imageLoader = ImageLoader.Builder(LocalContext.current)
                    .memoryCache {
                        MemoryCache.Builder(context)
                            .maxSizePercent(0.25)
                            .build()
                    }
                    .diskCache {
                        DiskCache.Builder()
                            .directory(context.cacheDir.resolve("image_cache"))
                            .maxSizePercent(0.02)
                            .build()
                    }
                    .build()
            )
            Text(text = opponentUser.userName.value)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 15.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                reverseLayout = true,
                state = lazyColumnState
            ) {
                items(
                    localDialogSnapshot,
                    key = { index -> "${index.date}_${index.messageText}" },
                    itemContent = { index ->
                        val message = index
                        val localDateTime =
                            message.date.split(" ").get(1)
                                .split(":")
                        Message(
                            messageText = index.messageText
                                ?: "",
//                            messageDate = index.date
                                (localDateTime[0] ?: "") + ":" + (localDateTime[1] ?: "")
                            ,
                            Modifier.padding(5.dp),
//                            index % 2 == 1
                            (index.user == sendMessageViewModel.storage.userUID)
                        )
                    })
            }

//            WTF?
            this@Column.AnimatedVisibility(
                visible = canScrollDown, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Transparent)
                    .padding(7.dp)
            ) {
                Button(
                    modifier = Modifier
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        )
                        .size(60.dp), onClick = {
                        coroutineScope.launch {
                            lazyColumnState.animateScrollToItem(0)
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
                .fillMaxWidth()
                .imePadding()
                .sizeIn(maxHeight = 170.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = message, onValueChange = { message = it }, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 15.dp)
                    .onFocusChanged {
                        coroutineScope.launch {
                            lazyColumnState.animateScrollToItem(0)
                        }
                    }
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
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

//
//@Composable
//fun Message(
//    messageText: String,
//    messageDate: String,
//    modifier: Modifier = Modifier,
//    flagAddress: Boolean
//) {
//    Text(
//        text = "$messageText ($messageDate)",
//        modifier = modifier.padding(5.dp)
//    )
//}

@Composable
fun Message(
    messageText: String,
    messageDate: String,
    modifier: Modifier = Modifier,
    flagAddress: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {

            }) {
        Box(
            modifier = Modifier
                .align(if (flagAddress) Alignment.TopEnd else Alignment.TopStart)
                .fillMaxWidth(0.75f)
                .wrapContentWidth(align = if (flagAddress) Alignment.End else Alignment.Start)
                .background(
                    color = if (flagAddress) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(20.dp)
                )

        ) {
            Text(
                text = messageText,
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 10.dp, vertical = 15.dp)
            )
            Text(
                text = messageDate, fontSize = 10.sp, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = 2.dp)
                    .padding(horizontal = 10.dp)
            )
        }
    }
}