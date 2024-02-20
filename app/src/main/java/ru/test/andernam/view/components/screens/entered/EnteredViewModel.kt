package ru.test.andernam.view.components.screens.entered

import android.content.Context
import androidx.lifecycle.ViewModel
import ru.test.andernam.domain.newest.impl.AuthImpl
import javax.inject.Inject

class EnteredViewModel(val context: Context) : ViewModel() {

    @Inject
    lateinit var authClass: AuthImpl
    fun sendCode(phoneNumber: String){
        authClass.sendSMS(phoneNumber, context)
    }

    suspend fun returnCode(code: String):Boolean{
        return authClass.returnSMS(code)
    }
}