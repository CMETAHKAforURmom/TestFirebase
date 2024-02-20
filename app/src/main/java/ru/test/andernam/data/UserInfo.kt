package ru.test.andernam.data

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class UserInfo(
    var userId: MutableState<String>,
    var userName: MutableState<String>,
    var userImageHref: MutableState<Uri?>,
    var dialogsList: MutableState<String>
)

fun defaultUserInfo(userId: String): UserInfo{
    return UserInfo(mutableStateOf(userId), mutableStateOf("A"), mutableStateOf(null), mutableStateOf(""))
}