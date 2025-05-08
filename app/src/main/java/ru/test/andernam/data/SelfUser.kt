package ru.test.andernam.data

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf


data class SelfUser(
    var userId: String,
    var userName: MutableState<String>,
    var userImageHref: MutableState<Uri?>,
    var dialogs: MutableList<String>,
    //  MUTABLE MAP
    var messages: MutableMap<UserInfo, List<Message>>
)
fun emptySelfUser() = SelfUser("", mutableStateOf(""), mutableStateOf(Uri.EMPTY),
    mutableStateListOf(), mutableMapOf())