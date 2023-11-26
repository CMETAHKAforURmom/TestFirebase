package ru.test.andernam.view.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.test.andernam.view.components.screens.BlogComp
import ru.test.andernam.view.components.screens.MainComp
import ru.test.andernam.view.components.screens.SendMessageScreen
import ru.test.andernam.view.components.screens.entered.EnteredComposable
import ru.test.andernam.view.components.screens.entered.EnteredViewModel
import ru.test.andernam.view.ui_parts.Scaffold.isMessageShowHelper
import ru.test.andernam.view.ui_parts.Scaffold.isShowHelper
import javax.inject.Inject


class Navigator @Inject constructor(private val enteredViewModel: EnteredViewModel) {

    var isNavigatorAsked = false
    var defaultDestination = Routes.Enter.route

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        isNavigatorAsked = true
        setDefController(navController)
        NavHost(navController = navController, startDestination = defaultDestination) {
            composable(Routes.Enter.route) {
                EnteredComposable(enteredViewModel).EnteredComp()
                isShowHelper.value = false
            }
            composable(Routes.Main.route) {
                MainComp()
                isShowHelper.value = true
                isMessageShowHelper.value = false
            }
            composable(Routes.Blog.route) {
                BlogComp()
                isShowHelper.value = true
                isMessageShowHelper.value = false
            }
            composable(Routes.Message.route) {
                SendMessageScreen()
                isMessageShowHelper.value = true
            }
        }
    }
}

sealed class Routes(val route: String){
    object Enter: Routes("Start")
    object Main: Routes("Main")
    object Blog: Routes("Blog")
    object Message: Routes("Message")
    object Back: Routes("Back")
    object Def: Routes("Start")
}