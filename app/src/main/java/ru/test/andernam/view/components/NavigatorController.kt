package ru.test.andernam.view.components

import androidx.navigation.NavController

private var navController: NavController? = null
fun setDefController(navControllerLocal: NavController){
    navController = navControllerLocal
}

fun navigateTo(route: Routes){
    navController?.navigate(route.route)
}
