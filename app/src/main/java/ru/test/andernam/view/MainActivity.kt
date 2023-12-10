package ru.test.andernam.view

//import androidx.hilt.navigation.compose.hiltViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.test.andernam.domain.AuthThingClass
import ru.test.andernam.domain.LiveUserData
import ru.test.andernam.domain.UserClass
import ru.test.andernam.domain.ipl.DownloadUploadHelp
import ru.test.andernam.ui.theme.TestFirebaseTheme
import ru.test.andernam.view.components.Navigator
import javax.inject.Inject
import kotlin.concurrent.thread

lateinit var userClass: DownloadUploadHelp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @get:Provides
//    val context: LifecycleOwner = this
//    private val stringLive = LiveUserData(userClass)
//    private val stringObserver = Observer<ProfileInfo>{value ->
//        getString = value.name!!
//        println(getString)
//    }
//    private val lifecucleObserv = LifecycleObserverCustom()
//    private var getString = ""

    @Inject
    lateinit var navigator : Navigator

    @Inject
    lateinit var userClass: UserClass

    @Inject
    lateinit var authThingClass: AuthThingClass

    @Inject
    lateinit var userLiveData: LiveUserData

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            TestFirebaseTheme {
                navigator.Navigation(navController)
            }
            lifecycleScope.launchWhenCreated {
                userLiveData.screen.collectLatest {
                    navController.navigate(it)
                }
            }
        }


        thread {
            authThingClass.start(userLiveData)
        }
    }
}
