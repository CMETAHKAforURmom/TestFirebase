package ru.test.andernam.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.test.andernam.navigation.apis.CurrMessageApi
import ru.test.andernam.view.components.screens.SendMessageScreen

class CurrMessageImpl: CurrMessageApi {
    override val messageRoute: String = "curr_message"
    var messageParameterHelper = "message"
    var parameter = "parameter"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.navigation(route = messageRoute, startDestination = messageParameterHelper){
            composable(route = "$messageParameterHelper/{$parameter}", arguments = listOf(
                navArgument(parameter){type = NavType.StringType}
            )){
                SendMessageScreen()
            }
        }
    }
}