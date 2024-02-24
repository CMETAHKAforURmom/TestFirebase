package ru.test.andernam.view.ui_parts.Scaffold

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.test.andernam.AppModule.provideCurrMessageImpl
import ru.test.andernam.AppModule.provideHomeImpl
import ru.test.andernam.navigation.AppNavGraph
import ru.test.andernam.view.components.screens.sendMessage.SendMessageViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController
) {
    var showBottomBar by remember {
        mutableStateOf(false)
    }

    val sendViewModel = hiltViewModel<SendMessageViewModel>()

    var showTopBar by remember {
        mutableStateOf(false)
    }
    var messageScreen by remember {
        mutableStateOf(false)
    }
    val navDestination by navController.currentBackStackEntryAsState()

    showBottomBar = (navDestination?.destination?.route == provideHomeImpl().homeRoute ||
            navDestination?.destination?.route == provideHomeImpl().profileRoute ||
            navDestination?.destination?.route == provideHomeImpl().messagesRoute ||
            navDestination?.destination?.route == provideCurrMessageImpl().messageRoute)
    messageScreen = (navDestination?.destination?.route == provideCurrMessageImpl().messageRoute)
    showTopBar = (navDestination?.destination?.route == provideCurrMessageImpl().messageRoute)

    Scaffold(
        topBar = {
            AnimatedVisibility(visible = showTopBar) {
                TopAppBar(title = {
                    TopMessageScaffold(
                        back = { navController.navigateUp() },
                        userInfo = sendViewModel.storage.getUserDataByDialog()
                    )
                })
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = showBottomBar) {
                BottomAppBar(content = {
                    if (messageScreen)
                        BottomMessageScaffold { sendViewModel.sendMessage(it) }
                    else
                        BottomBarNavigation(
                            mapOf(
                                "Users" to {
                                    navController.navigate(
                                        provideHomeImpl().messagesRoute
                                    )
                                },
                                "Profile" to {
                                    navController.navigate(
                                        provideHomeImpl().profileRoute
                                    )
                                }
                            )
                        )
                }
                )
            }
        }) {
        AppNavGraph(modifier = Modifier.padding(it), navController = navController)
    }
}