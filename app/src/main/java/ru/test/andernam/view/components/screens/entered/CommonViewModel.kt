package ru.test.andernam.view.components.screens.entered

import androidx.lifecycle.ViewModel
import ru.test.andernam.AppModule.provideAuthClass
import ru.test.andernam.domain.AuthThingClass
import javax.inject.Inject

class CommonViewModel @Inject constructor(): ViewModel() {

    val authThingClass = provideAuthClass()
    fun sendCode(phoneNumber: String){
        authThingClass.enterAcc(phoneNumber)
    }

    fun returnCode(code: String, action: () -> Unit){
        if(authThingClass.signInWithCode(code))
            action
    }
}