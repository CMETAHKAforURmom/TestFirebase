package ru.test.andernam.view.ui_parts.Scaffold

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.data.UserInfo
import ru.test.andernam.view.components.screens.messages.messageList.CardElementUser

@Composable
fun TopMessageList(actionToGo: () -> Unit, storage: DatabaseVariables, testAction: () -> Unit) {

    var isSearching by remember {
        mutableStateOf(false)
    }
    var allUsers by remember {
        mutableStateOf<List<UserInfo>>(emptyList())
    }
    var viewUsers by remember {
        mutableStateOf<List<UserInfo>>(allUsers)
    }
    var searchText by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(48.dp).clickable {
                    testAction.invoke()
                })
        }
    }
}

@Composable
fun TopMessageScaffold(
    back: () -> Unit,
    userInfo: UserInfo
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(64.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.clickable {
                back.invoke()
            })
        AsyncImage(
            model = userInfo.userImageHref.value, contentDescription = "Image profile",
            modifier = Modifier
                .padding(15.dp)
                .size(54.dp)
                .clip(CircleShape)
        )
        Text(text = userInfo.userName.value)
    }
}

@Composable
fun BottomMessageScaffold(actionSend: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .height(135.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = message, onValueChange = { message = it }, modifier = Modifier
                .fillMaxWidth(0.8f)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(35f))
        )
        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send action",
            modifier = Modifier
                .clickable {
                    actionSend(message)
                    message = ""
                }
                .size(42.dp))
    }
}