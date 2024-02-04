package ru.test.andernam.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.AppModule.provideMessageImpl
import ru.test.andernam.navigation.apis.HomeApi
import ru.test.andernam.view.components.screens.entered.EnteredComp

class HomeImpl: HomeApi {

    override val homeRoute: String = "home"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(homeRoute){
            EnteredComp(onNavigateToMessages = { navController.navigate(provideMessageImpl().messageRoute){
                popUpTo(homeRoute){inclusive = true}
            } })
        }
    }

}