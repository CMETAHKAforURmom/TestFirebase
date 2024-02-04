package ru.test.andernam.view.components

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import ru.test.andernam.R

private var navController: NavController? = null
fun setDefController(navControllerLocal: NavController){
    navController = navControllerLocal
}

fun navigateTo(route: String){
    if(route == "back")
        navController?.navigateUp()
    else
        navController?.navigate(route)
}
