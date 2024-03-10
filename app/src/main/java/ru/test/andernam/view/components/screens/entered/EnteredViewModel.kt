package ru.test.andernam.view.components.screens.entered

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.test.andernam.domain.impl.AuthImpl
import javax.inject.Inject

@HiltViewModel
class EnteredViewModel @Inject constructor () : ViewModel() {

    @Inject
    lateinit var authClass: AuthImpl
    fun sendCode(phoneNumber: String, context: Context){
        authClass.sendSMS(phoneNumber, context)
    }

    fun checkEnter(): Boolean{
        return authClass.checkEnter()
    }

    fun signWithGoogle(context: Context){
//        authClass.signInWithGoogle()
    }

    suspend fun returnCode(code: String):Boolean{
        return authClass.returnSMS(code)
    }
}