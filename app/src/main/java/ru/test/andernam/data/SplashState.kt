package ru.test.andernam.data

sealed class SplashState {
    object Loading : SplashState()
    object Success : SplashState()
    object NotAuthenticated : SplashState()
    data class Error(val message: String): SplashState()
}