package ru.test.andernam.navigation.impls

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.test.andernam.AppModule.provideEnterImpl
import ru.test.andernam.AppModule.provideHomeImpl
import ru.test.andernam.navigation.apis.FeatureApi
import ru.test.andernam.view.components.screens.splash.SplashHostComposable
import ru.test.andernam.view.components.screens.splash.SplashViewModel

class SplashImpl: FeatureApi {
    val route = "splash"
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route = route){
            SplashHostComposable(hiltViewModel<SplashViewModel>(),
                { navController.navigate(provideHomeImpl().profileRoute) },
                { navController.navigate(provideEnterImpl().enterRoute) })
        }
    }
}