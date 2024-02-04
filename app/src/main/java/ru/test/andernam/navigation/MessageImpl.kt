package ru.test.andernam.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.navigation.apis.MessageApi
import ru.test.andernam.view.components.screens.BlogComp

class MessageImpl: MessageApi {
    override val messageRoute: String = "messages"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(messageRoute){
            BlogComp()
        }
    }
}