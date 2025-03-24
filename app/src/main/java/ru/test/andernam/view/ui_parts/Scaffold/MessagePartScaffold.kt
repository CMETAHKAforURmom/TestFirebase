package ru.test.andernam.view.ui_parts.Scaffold

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateListOf
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
import ru.test.andernam.view.components.screens.messages.CardElementUser

@Composable
fun TopMessageList(actionToGo: () -> Unit, storage: DatabaseVariables) {

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
    val focusRequester = remember { FocusRequester() }

    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight()
                .size(128.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchText, onValueChange = {
                    searchText = it
                },
                modifier = Modifier
                    .height(64.dp)
                    .padding(0.dp)
                    .fillMaxWidth(0.9f)
                    .focusRequester(focusRequester)
                    .focusTarget()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && searchText.isNotEmpty()) {
                            isSearching = true
                        } else if (!focusState.isFocused) {
                            isSearching = false
                        }
                    },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    if(searchText.isNotEmpty()){
                        viewUsers = allUsers.filter {
                            it.userName.value.contains(
                                searchText,
                                ignoreCase = true
                            )
                        }
                        isSearching = true
                    }
                })
            )

            LaunchedEffect(Dispatchers.Default) { allUsers = storage.getAllUsers()
                if(isSearching)
                    coroutineScope.launch { focusRequester.requestFocus() }}
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.clickable {
                    viewUsers = allUsers.filter {
                    it.userName.value.contains(
                        searchText,
                        ignoreCase = true
                    )
                }
                    isSearching = true
                })
        }
        DropdownMenu(
            expanded = isSearching,
            onDismissRequest = { isSearching = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewUsers.forEach { user ->
                    CardElementUser(
                        user,
                        actionToGo = { dialogHref ->
                            actionToGo.invoke()
                            storage.selectDialogHref(dialogHref)
                        },
                        methodGetHref = { opponentUser -> storage.startMessaging(opponentUser) })
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
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