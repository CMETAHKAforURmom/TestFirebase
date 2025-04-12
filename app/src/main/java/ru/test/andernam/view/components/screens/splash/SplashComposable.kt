package ru.test.andernam.view.components.screens.splash

import android.app.Activity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import ru.test.andernam.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import ru.test.andernam.view.theme.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.size.Scale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import ru.test.andernam.data.SplashState
import ru.test.andernam.data.SplashState.*

@Composable
fun SplashHostComposable(
    splashViewModel: SplashViewModel,
    onProfile: () -> Unit,
    onEnter: () -> Unit
) {
    val state = splashViewModel.state.collectAsState()
    LaunchedEffect(state) {
        when (state.value) {
            is Success -> onProfile.invoke()
            is NotAuthenticated -> onEnter.invoke()
            else -> {}
        }
    }
    SplashComposable()
}

@Composable
fun SplashComposable() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }
    val rotationAngle by rememberInfiniteTransition(label = "rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightDark)
            .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
    ) {
        AnimatedVisibility(visible = visible, enter = fadeIn(animationSpec = tween(durationMillis = 1000)), // Плавное появление за 500 мс
            exit = fadeOut(animationSpec = tween(durationMillis = 0))) {
            Image(
                painter = painterResource(id = R.drawable.forum),
                contentDescription = "App logo",
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(55.dp)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}