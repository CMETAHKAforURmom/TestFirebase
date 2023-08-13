package ru.test.andernam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import ru.test.andernam.domain.start
import ru.test.andernam.ui.theme.TestFirebaseTheme
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reslover = contentResolver
        contextActivity = this
        thread {
            start()
        }
        setContent {
            TestFirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null)
            user = auth.currentUser
    }
}
