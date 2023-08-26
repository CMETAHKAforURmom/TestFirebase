package ru.test.andernam.view.ui_parts.Scaffold

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.test.andernam.R
import ru.test.andernam.view.components.Navigation
import ru.test.andernam.view.components.Routes
import ru.test.andernam.view.components.navigateTo

var isShowHelper = mutableStateOf(false)
var isMessageShowHelper = mutableStateOf(true)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScaffold() {

    var isShow by remember{
        mutableStateOf(false)
    }

    var isMessage by remember{
        mutableStateOf(false)
    }

    isShow = isShowHelper.value
    isMessage = isMessageShowHelper.value

    var defaultMapForBottomNavigation = mapOf("Users" to { navigateTo(Routes.Blog) }, "Profile" to {navigateTo(Routes.Main)})
    Scaffold(topBar = { AnimatedVisibility(visible = isMessage) {
        TopAppBar(title = {TopMessageScaffold()})
    }},
        bottomBar = {
        AnimatedVisibility(visible = isShow) {
            BottomAppBar(content = {
                if (isMessage)
                    BottomMessageScaffold()
                else
                    BottomBarNavigation(defaultMapForBottomNavigation)
            })
        }
    }) {
        Box(modifier = Modifier.padding(vertical = 30.dp), contentAlignment = Alignment.Center) {
            Navigation()
        }
    }
}