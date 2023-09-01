package ru.test.andernam.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import ru.test.andernam.domain.start
import ru.test.andernam.ui.theme.TestFirebaseTheme
import ru.test.andernam.view.ui_parts.Scaffold.mainScaffold
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestFirebaseTheme {

                mainScaffold()
            }
        }
        thread {
            start(this)
        }
    }
}
