package ru.test.andernam.navigation.impls

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.AppModule.provideHomeImpl
import ru.test.andernam.navigation.apis.EnterApi
import ru.test.andernam.view.components.screens.entered.EnteredComp
import ru.test.andernam.view.components.screens.entered.EnteredViewModel

class EnterImpl: EnterApi {
    override val enterRoute: String = "enter"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(enterRoute){
            EnteredComp(onNavigateToMessages = {navController.navigate(provideHomeImpl().profileRoute)}, enteredViewModel = hiltViewModel<EnteredViewModel>())
        }
    }
}