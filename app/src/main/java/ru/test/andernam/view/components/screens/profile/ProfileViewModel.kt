package ru.test.andernam.view.components.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.test.andernam.data.DatabaseVariables
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val storage: DatabaseVariables) : ViewModel() {

    fun exitAccount() {
        storage.auth.signOut()
        storage.user = null
    }

    private suspend fun downloadThisProfile() {
        storage.getThisUser()
    }

    fun saveUserData(imageHref: Uri, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            storage.uploadUserInfo(imageHref, name)
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            downloadThisProfile()
        }
    }
}