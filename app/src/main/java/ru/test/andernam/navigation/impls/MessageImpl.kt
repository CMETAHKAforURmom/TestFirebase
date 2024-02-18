package ru.test.andernam.navigation.impls

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.test.andernam.navigation.apis.MessageApi
import ru.test.andernam.view.components.screens.SendMessageScreen

class MessageImpl: MessageApi {
    override val messageRoute: String = "messages"
    override var hrefDialog: String = "empty"
    override var messageHref = "$messageRoute/{$hrefDialog}"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(messageHref, arguments = listOf(navArgument(hrefDialog){
            type = NavType.StringType
        })){
            SendMessageScreen(requireNotNull(it.arguments?.getString(messageHref)))
        }
    }
}