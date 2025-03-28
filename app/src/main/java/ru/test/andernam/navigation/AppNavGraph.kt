package ru.test.andernam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.test.andernam.AppModule.provideCurrMessageImpl
import ru.test.andernam.AppModule.provideEnterImpl
import ru.test.andernam.AppModule.provideHomeImpl
import ru.test.andernam.navigation.apis.FeatureApi

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = provideEnterImpl().enterRoute, modifier) {
        register(
            provideHomeImpl(),
            navController = navController
        )

        register(
            provideEnterImpl(),
            navController = navController
        )
        register(
            provideCurrMessageImpl(),
            navController = navController
        )
    }
}

fun NavGraphBuilder.register(
    featureApi: FeatureApi,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    featureApi.registerGraph(
        navGraphBuilder = this,
        navController = navController,
        modifier = modifier
    )
}