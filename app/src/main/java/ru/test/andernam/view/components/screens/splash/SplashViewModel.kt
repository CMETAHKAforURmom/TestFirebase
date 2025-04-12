package ru.test.andernam.view.components.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.data.SplashState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val _storage: DatabaseVariables) : ViewModel(){
    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state = _state.asStateFlow()
    val user = _storage.userUID

    init {
        if(user != null) {
            _state.value = SplashState.Success
            Log.i("Splash state is","${_state.value}")
        }
        else _state.value = SplashState.NotAuthenticated
    }
}