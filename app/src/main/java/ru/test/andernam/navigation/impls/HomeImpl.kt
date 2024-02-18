package ru.test.andernam.navigation.impls

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.navigation.apis.HomeApi
import ru.test.andernam.view.components.screens.BlogComp
import ru.test.andernam.view.components.screens.Main.MainComp

class HomeImpl: HomeApi {

    override val homeRoute = "home"
    override val profileRoute = "$homeRoute/profile"
    override val messagesRoute = "$homeRoute/messages"
    val startRoute = messagesRoute

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
//        navGraphBuilder.composable(homeRoute){
//            EnteredComp(onNavigateToMessages = { navController.navigate(provideMessageImpl().messageRoute){
//                popUpTo(homeRoute){inclusive = true}
//            } })
//        }
        navGraphBuilder.composable(profileRoute){
            MainComp()
        }
        navGraphBuilder.composable(messagesRoute){
            BlogComp(navController)
        }
    }

}