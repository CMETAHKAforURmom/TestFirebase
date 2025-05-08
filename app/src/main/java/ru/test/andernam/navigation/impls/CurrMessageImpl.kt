package ru.test.andernam.navigation.impls

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.navigation.apis.CurrMessageApi
import ru.test.andernam.view.components.screens.messages.messageScreen.SendMessageScreen
import ru.test.andernam.view.components.screens.messages.messageScreen.SendMessageViewModel

class CurrMessageImpl : CurrMessageApi {
    override val messageRoute: String = "curr_message"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route = messageRoute) {
            SendMessageScreen(hiltViewModel<SendMessageViewModel>(), {navController.navigateUp()})
        }
    }
}
