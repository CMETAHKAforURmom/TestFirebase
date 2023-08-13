package ru.test.andernam

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AUTH
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.test.andernam.domain.signInWithPhoneAuthCredential
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnteredComp(navController: NavController) {

    var isCodeSendet by remember {
        mutableStateOf(false)
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var code by remember {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()

    if(user != null)
        navController.navigate(Routes.Main.route)

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Registration")
            TextField(value = phoneNumber, onValueChange = { phoneNumber = it },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 35.dp), label = { "Phone" })
            Button(
                onClick = {
                    isCodeSendet = true
                    enterAcc(phoneNumber, callbacks)},
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Text(text = "Send!")
            }
        }

        AnimatedVisibility(visible = isCodeSendet) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Enter code", color = Color.White)

                TextField(value = code, onValueChange = { code = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 35.dp), label = { "Code" })
                Button(
                    onClick = {
                        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
                        signInWithPhoneAuthCredential(credential)
                        coroutineScope.launch {

                            delay(1000L)
                            if(user != null)
                                navController.navigate(Routes.Wait.route)
                            else
                                delay(1000L)
                            if(user != null)
                                navController.navigate(Routes.Wait.route)
                            }
                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                ) {
                    Text(text = "Sign in!")
                }
            }
        }
        }
    }
}

fun enterAcc(phone: String, callbacks: OnVerificationStateChangedCallbacks){
    
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phone) // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(contextActivity) // Activity (for callback binding)
        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}
