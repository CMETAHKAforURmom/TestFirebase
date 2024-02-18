package ru.test.andernam.view.ui_parts.Scaffold

//import ru.test.andernam.view.components.Navigation
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.test.andernam.AppModule.provideHomeImpl
import ru.test.andernam.AppModule.provideStrings
import ru.test.andernam.navigation.AppNavGraph


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

    var showTopBar by remember {
        mutableStateOf(false)
    }
    val navDestination by navController.currentBackStackEntryAsState()

    showBottomBar = (navDestination?.destination?.route == provideHomeImpl().homeRoute ||
            navDestination?.destination?.route == provideHomeImpl().profileRoute ||
            navDestination?.destination?.route == provideHomeImpl().messagesRoute)
    showTopBar = (navDestination?.destination?.route == provideStrings().messageStringNavigation)

//    val messages = LocalContext.current.getString(R.string.messages)
//    val profile = LocalContext.current.getString(R.string.profile)
//    val defaultMapForBottomNavigation = mapOf("Users" to { navigateTo(messages) }, "Profile" to {navigateTo(profile)})
    Scaffold(
        topBar = {
            AnimatedVisibility(visible = showTopBar) {
                TopAppBar(title = { TopMessageScaffold() })
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = showBottomBar) {
                BottomAppBar(content = {
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
        AppNavGraph(navController = navController)
    }
}