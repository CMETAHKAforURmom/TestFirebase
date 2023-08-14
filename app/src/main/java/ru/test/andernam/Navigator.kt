package ru.test.andernam

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private var navController: NavController? = null
var isNavigatorAskd = false

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation() {
    navController = rememberNavController()
    isNavigatorAskd = true
    NavHost(navController = navController as NavHostController, startDestination = Routes.Enter.route){
        composable(Routes.Enter.route){
            EnteredComp(navController!!)
        }
        composable(Routes.Main.route){
            MainComp(navController = navController!!)
        }
    }
}

fun getNavController() : NavController = navController!!

sealed class Routes(val route: String){
    object Enter: Routes("Start")
    object Main: Routes("Main")
}