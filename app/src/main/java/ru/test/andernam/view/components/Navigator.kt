package ru.test.andernam.view.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.test.andernam.view.components.screens.Main.MainComposable
import ru.test.andernam.view.components.screens.SendMessageScreen
import ru.test.andernam.view.components.screens.entered.EnteredComposable
import ru.test.andernam.view.ui_parts.Scaffold.isMessageShowHelper
import ru.test.andernam.view.ui_parts.Scaffold.isShowHelper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Navigator @Inject constructor() {

    @Inject
    lateinit var enteredComposable: EnteredComposable

//    @Inject
//    lateinit var blogComposable: BlogComposable

    @Inject
    lateinit var mainComposable: MainComposable

    var isNavigatorAsked = false
    var defaultDestination = Routes.Main.route

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun Navigation(navController: NavHostController) {
        isNavigatorAsked = true
        setDefController(navController)
        NavHost(navController = navController, startDestination = defaultDestination) {
            composable(Routes.Enter.route) {
                enteredComposable.EnteredComp()
                isShowHelper.value = false
            }
            composable(Routes.Main.route) {
                mainComposable.MainComp()
                isShowHelper.value = true
                isMessageShowHelper.value = false
            }
            composable(Routes.Blog.route) {
//                blogComposable.BlogComp()
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