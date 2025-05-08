package ru.test.andernam.view.components.screens.users.search

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.test.andernam.view.components.screens.messages.messageList.CardElementUser

@Composable
fun SearchUsersComposable(actionToGo: () -> Unit, searchUsersViewModel: SearchUsersViewModel) {

    var isSearching by remember {
        mutableStateOf(false)
    }
    var allUsers = searchUsersViewModel.allUsers

    var searchText by remember {
        mutableStateOf("")
    }

    var viewUsers by remember {
        mutableStateOf(allUsers.value)
    }

    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = searchText, onValueChange = {
                    searchText = it
                    if (searchText.isNotEmpty()) {
                        viewUsers = allUsers.value.filter {
                            it.userName.value.contains(
                                searchText,
                                ignoreCase = true
                            )
                        }
                        isSearching = true
                    } else viewUsers = allUsers.value
                },
                modifier = Modifier
                    .height(48.dp)
                    .padding(0.dp)
                    .fillMaxWidth(0.8f)
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
                )
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(48.dp))
        }

        LazyColumn(
            modifier = Modifier
                .padding(15.dp)
        ) {
            items(viewUsers.size, itemContent = {
                CardElementUser(
                    viewUsers[it],
                    actionToGo = { dialogHref ->
                        actionToGo.invoke()
                        searchUsersViewModel.selectDialog(dialogHref)
                    },
                    methodGetHref = { opponentUser ->
                        searchUsersViewModel.startMessaging(opponentUser)
                    })
            })
        }

    }
}