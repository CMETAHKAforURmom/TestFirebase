package ru.test.andernam.view.ui_parts

import android.graphics.Paint.Align
import android.widget.Space
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.test.andernam.R
import ru.test.andernam.domain.enterAcc
import ru.test.andernam.domain.signInWithCode


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnteredComp() {

    var expandedPhoneSelect by remember {
        mutableStateOf(false)
    }

    val options: Map<String, Painter> = mapOf<String, Painter>(
        "+7" to painterResource(id = R.drawable.russia),
        "+380" to painterResource(id = R.drawable.ukraine),
        "+375" to painterResource(id = R.drawable.belarus)
    )

    var defaultFlag = painterResource(id = R.drawable.russia)

    var selectedOptionText by remember {
        mutableStateOf(Pair("+7", options.getOrDefault("+7", defaultFlag)))
    }

    var isCodeSendet by remember {
        mutableStateOf(false)
    }

    var phoneNumber by remember {
        mutableStateOf("9123456789")
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
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(135.dp)
                    .padding(horizontal = 25.dp, vertical = 35.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(35f))
//                    .height(60.dp)
                    .width(120.dp)
                    .fillMaxHeight()
                    .clickable {
                        expandedPhoneSelect = true
                    }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
                    Image(
                        painter = selectedOptionText.second,
                        contentDescription = "Country",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = selectedOptionText.first, modifier = Modifier)
                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24), contentDescription = "Arrow")
                }
                }
                DropdownMenu(
                    expanded = expandedPhoneSelect,
                    onDismissRequest = { expandedPhoneSelect = false }
                ) {
                    options.forEach { country ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                    Image(
                                        painter = country.value,
                                        contentDescription = "country",
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(country.key)
                                }
                            },
                            onClick = {
                                selectedOptionText = selectedOptionText.copy(
                                    first = country.key,
                                    second = country.value
                                )
                                expandedPhoneSelect = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it },
                    modifier = Modifier
                        .fillMaxHeight(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    label = { "Phone" })
            }

            Button(
                onClick = {
                    isCodeSendet = true
                    enterAcc("${selectedOptionText.first}$phoneNumber")
                },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Text(text = "Send!")
            }
        }

        AnimatedVisibility(visible = isCodeSendet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.DarkGray)
            ) {
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

