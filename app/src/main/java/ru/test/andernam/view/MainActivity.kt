package ru.test.andernam.view

//import androidx.hilt.navigation.compose.hiltViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import ru.test.andernam.domain.ipl.DownloadUploadHelp
import ru.test.andernam.ui.theme.TestFirebaseTheme
import ru.test.andernam.view.components.Navigator
import javax.inject.Inject
import kotlin.concurrent.thread

lateinit var userClass: DownloadUploadHelp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator : Navigator

    private val lifecucleObserv = LifecycleObserverCustom()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(lifecucleObserv)

        setContent {
            TestFirebaseTheme {
//                val viewModel = hiltViewModel<EnteredViewModel>()
                navigator.Navigation()
            }
        }
        thread {
//
//            userClass = start()
        }
    }
}
