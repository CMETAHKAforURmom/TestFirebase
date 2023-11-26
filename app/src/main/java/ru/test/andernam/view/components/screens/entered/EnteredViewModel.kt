package ru.test.andernam.view.components.screens.entered

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.test.andernam.domain.AuthThingClass
import javax.inject.Inject

class EnteredViewModel @Inject constructor(private val authThingClass: AuthThingClass): ViewModel() {

    private val _uiState = MutableStateFlow(EnteredUiState())
    val uiState: StateFlow<EnteredUiState> = _uiState.asStateFlow()


    fun tryEnter(phoneNumber: String){
        authThingClass.enterAcc(phoneNumber)
    }


}