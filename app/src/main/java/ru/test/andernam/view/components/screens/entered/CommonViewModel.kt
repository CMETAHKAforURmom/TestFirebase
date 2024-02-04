package ru.test.andernam.view.components.screens.entered

import androidx.lifecycle.ViewModel
import ru.test.andernam.AppModule.provideAuthClass
import javax.inject.Inject

class CommonViewModel @Inject constructor(): ViewModel() {

    val authThingClass = provideAuthClass()
    fun sendCode(phoneNumber: String){
        authThingClass.enterAcc(phoneNumber)
    }

    fun returnCode(code: String):Boolean{
        return (authThingClass.signInWithCode(code))

    }
}