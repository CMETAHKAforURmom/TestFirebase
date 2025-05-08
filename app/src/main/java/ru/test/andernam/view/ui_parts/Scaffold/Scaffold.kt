package ru.test.andernam.view.ui_parts.Scaffold

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import ru.test.andernam.AppModule.provideCurrMessageImpl
import ru.test.andernam.AppModule.provideHomeImpl
import ru.test.andernam.AppModule.provideSearchImpl
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.navigation.AppNavGraph
import ru.test.andernam.view.theme.*


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    storage: DatabaseVariables
) {
    var showBottomBar by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    var showOpponentTopInfo by remember { mutableStateOf(false) }
    var messageScreen by remember { mutableStateOf(false) }
    var showSearchUser by remember { mutableStateOf(false) }
    val navDestination by navController.currentBackStackEntryAsState()

    val window = ((LocalContext.current) as? Activity)?.window
    val isDark = isSystemInDarkTheme()
//    SideEffect {
//        window?.apply {
//            setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//            insetsController?.setSystemBarsAppearance(
//                if (isDark) {
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS.inv()
//                } else {
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                },
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//            )
//        }
//    }

    showBottomBar = (navDestination?.destination?.route == provideHomeImpl().homeRoute ||
            navDestination?.destination?.route == provideHomeImpl().profileRoute ||
            navDestination?.destination?.route == provideHomeImpl().messagesRoute ||
            navDestination?.destination?.route == provideCurrMessageImpl().messageRoute)
    showSearchUser = (navDestination?.destination?.route == provideHomeImpl().messagesRoute)
    messageScreen = (navDestination?.destination?.route == provideCurrMessageImpl().messageRoute)
    showOpponentTopInfo =
        (navDestination?.destination?.route == provideCurrMessageImpl().messageRoute || showSearchUser)

    Scaffold(modifier = Modifier.imePadding()
//        topBar = {
//            AnimatedVisibility(visible = showOpponentTopInfo) {
//                TopAppBar(modifier = Modifier.padding(top = 30.dp), title = {
//                    if (showSearchUser)
////                        TopMessageList(actionToGo = {navController.navigate(provideCurrMessageImpl().messageRoute)}, storage, testAction = {navController.navigate(provideSearchImpl().route)})
//                    else
//                        TopMessageScaffold(
//                            back = { navController.navigateUp() },
//                            userInfo = storage.opponentUser
//                        )
//                })
//            }
//        },
//        bottomBar = {
//            AnimatedVisibility(visible = showBottomBar) {
//                BottomAppBar(content = {
////                    if (messageScreen)
////                        BottomMessageScaffold {  coroutine.launch { storage.sendMessage(it)} }
////                    else
////                        BottomBarNavigation(
////                            mapOf(
////                                "Users" to {
////                                    navController.navigate(
////                                        provideHomeImpl().messagesRoute
////                                    )
////                                },
////                                "Profile" to {
////                                    navController.navigate(
////                                        provideHomeImpl().profileRoute
////                                    )
////                                }
////                            )
////                        )
//                }
//                )
//            }
//        }
    ) {
        AppNavGraph(modifier = Modifier.padding(it).imePadding(), navController = navController)
    }
}