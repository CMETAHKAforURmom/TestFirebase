package ru.test.andernam

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.test.andernam.domain.enterAcc
import ru.test.andernam.domain.signInWithCode

var isUserExist = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnteredComp(navController: NavController) {

    if(isUserExist)
        navController.navigate(Routes.Main.route)

    var isCodeSendet by remember {
        mutableStateOf(false)
    }

    var phoneNumber by remember {
        mutableStateOf("+79123456789")
    }

    var code by remember {
        mutableStateOf("000000")
    }


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
                    enterAcc(phoneNumber)},
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
                        signInWithCode(code)
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
