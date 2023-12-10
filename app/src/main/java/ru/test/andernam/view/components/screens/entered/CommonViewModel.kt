package ru.test.andernam.view.components.screens.entered

import androidx.lifecycle.ViewModel
import ru.test.andernam.domain.AuthThingClass
import javax.inject.Inject

class CommonViewModel @Inject constructor(private val authThingClass: AuthThingClass): ViewModel() {

    fun sendCode(phoneNumber: String){
        authThingClass.enterAcc(phoneNumber)
    }

    fun returnCode(code: String){
        authThingClass.signInWithCode(code)
    }
}