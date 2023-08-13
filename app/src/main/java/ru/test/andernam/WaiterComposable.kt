package ru.test.andernam

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun checkComp(navController: NavController) {

    if(user!!.phoneNumber == null){
        Text(text = "Please wait, data is loading")
    }else
        navController.navigate(Routes.Main.route)

}