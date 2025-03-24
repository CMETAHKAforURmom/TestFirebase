package ru.test.andernam.view.components.screens.entered

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.test.andernam.domain.impl.AuthImpl
import javax.inject.Inject

@HiltViewModel
class EnteredViewModel @Inject constructor () : ViewModel() {

    @Inject
    lateinit var authClass: AuthImpl
    @Inject
    lateinit var credentialManager: CredentialManager

    fun sendCode(phoneNumber: String, context: Context){
        authClass.sendSMS(phoneNumber, context)
    }

    fun checkEnter(): Boolean{
        return authClass.checkEnter()
    }

    suspend fun returnCode(code: String):Boolean{
        return authClass.returnSMS(code)
    }

    fun signInWithGoogle(context: Context, onSuccess: () -> Unit){
        viewModelScope.launch {
            if(authClass.signInWithGoogle(context).isSuccess) onSuccess.invoke()
        }
    }

}