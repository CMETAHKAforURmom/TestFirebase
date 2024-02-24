package ru.test.andernam.domain.api

import android.content.Context

interface AuthApi {
    fun sendSMS(phone: String, context: Context)
    suspend fun returnSMS(code: String): Boolean

}