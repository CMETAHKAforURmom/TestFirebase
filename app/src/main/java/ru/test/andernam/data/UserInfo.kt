package ru.test.andernam.data

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

data class UserInfo(
    var userId: String,
    var userName: MutableState<String>,
    var userImageHref: MutableState<Uri?>,
    var dialogsList: MutableList<String>
)
fun defaultUserInfo(): UserInfo{
    return UserInfo((""), mutableStateOf(""), mutableStateOf(null), mutableStateListOf(""))
}

fun getDialogId(user: UserInfo, thisUser: String): String{
    var findingHref = ""
    user.dialogsList.forEach {
        if(it.contains(thisUser))
            findingHref = it.split("|")[1]
    }
    return findingHref
}