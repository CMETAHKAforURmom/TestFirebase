package ru.test.andernam.domain.newest.api

import android.net.Uri
import ru.test.andernam.data.UserInfo

interface CloudDatabaseAccessApi {

    suspend fun downloadProfile(userId: String): UserInfo

    suspend fun uploadUserInfo(imageHref: Uri, name: String, userId: String): Result<String>

    suspend fun sendMessage(message: String, userId: String, messageLink: String)
}