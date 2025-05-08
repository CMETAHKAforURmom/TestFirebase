package ru.test.andernam.navigation.impls

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.AppModule.provideCurrMessageImpl
import ru.test.andernam.navigation.apis.SearchUsersApi
import ru.test.andernam.view.components.screens.users.search.SearchUsersComposable
import ru.test.andernam.view.components.screens.users.search.SearchUsersViewModel

class SearchUsersImpl : SearchUsersApi {
    override val route = "search"
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route) {
            SearchUsersComposable(actionToGo = {navController.navigate(provideCurrMessageImpl().messageRoute)},hiltViewModel<SearchUsersViewModel>())
        }
    }
}