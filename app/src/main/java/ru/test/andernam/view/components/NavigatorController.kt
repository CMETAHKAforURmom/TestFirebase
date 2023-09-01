package ru.test.andernam.view.components

import androidx.navigation.NavController

private var navController: NavController? = null
fun setDefController(navControllerLocal: NavController){
    navController = navControllerLocal
}

fun navigateTo(route: Routes){
    if(route == Routes.Back)
        navController?.navigateUp()
    else
        navController?.navigate(route.route)
}
