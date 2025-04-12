package ru.test.andernam.navigation.impls

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.AppModule.provideCurrMessageImpl
import ru.test.andernam.AppModule.provideEnterImpl
import ru.test.andernam.navigation.apis.HomeApi
import ru.test.andernam.view.components.screens.messages.BlogComp
import ru.test.andernam.view.components.screens.messages.MessageListViewModel
import ru.test.andernam.view.components.screens.profile.MainComp
import ru.test.andernam.view.components.screens.profile.ProfileViewModel

class HomeImpl : HomeApi {

    override val homeRoute = "home"
    override val profileRoute = "$homeRoute/profile"
    override val messagesRoute = "$homeRoute/messages"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(profileRoute) {
            MainComp(hiltViewModel<ProfileViewModel>()
            ) { navController.navigate(provideEnterImpl().enterRoute) }
        }
        navGraphBuilder.composable(messagesRoute) {
            BlogComp(
                {navController.navigate(provideCurrMessageImpl().messageRoute)},
                hiltViewModel<MessageListViewModel>()
            )
        }
    }
}