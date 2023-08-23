package ru.test.andernam.view.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.test.andernam.view.ui_parts.BlogComp
import ru.test.andernam.view.ui_parts.EnteredComp
import ru.test.andernam.view.ui_parts.MainComp
import ru.test.andernam.view.ui_parts.Scaffold.isShowHelper

var isNavigatorAskd = false

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation() {
    var navController = rememberNavController()
    isNavigatorAskd = true
    setDefController(navController!!)
    NavHost(navController = navController as NavHostController, startDestination = Routes.Enter.route){
        composable(Routes.Enter.route){
            EnteredComp()
            isShowHelper.value = false
        }
        composable(Routes.Main.route){
            MainComp()
            isShowHelper.value = true
        }
        composable(Routes.Blog.route){
            BlogComp()
        }
    }
}

sealed class Routes(val route: String){
    object Enter: Routes("Start")
    object Main: Routes("Main")
    object Blog: Routes("Blog")
}