package ru.test.andernam.view.components.screens.entered

import android.content.Context
import androidx.lifecycle.ViewModel
import ru.test.andernam.AppModule.provideAuthImpl

class CommonViewModel(private val context: Context) : ViewModel() {

    val authClass = provideAuthImpl()
    fun sendCode(phoneNumber: String){
        authClass.sendSMS(phoneNumber, context)
    }

    suspend fun returnCode(code: String):Boolean{
        return authClass.returnSMS(code)
    }
}