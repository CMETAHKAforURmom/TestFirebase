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
import ru.test.andernam.AppModule.provideUserLiveData
import ru.test.andernam.domain.AuthThingClass
import ru.test.andernam.domain.old.UserClass
import ru.test.andernam.domain.old.ipl.DownloadUploadHelp
import ru.test.andernam.domain.repository.LiveUserData
import ru.test.andernam.navigation.AppNavGraph
import ru.test.andernam.view.components.Navigator
import ru.test.andernam.view.theme.TestFirebaseTheme
import javax.inject.Inject

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

    private var userLiveData: LiveUserData = provideUserLiveData()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            TestFirebaseTheme {
//                navigator.Navigation(navController)
                AppNavGraph(navController = navController)

            }
            lifecycleScope.launchWhenCreated {
                userLiveData.isAuthPassed.collectLatest {
                    if(it)
                        navController.navigate("messages")
                }
            }
//            authThingClass.start(userLiveData)
//            lifecycleScope.launchWhenCreated {
//                userLiveData.stateFlowScreen.collectLatest {
//                    navController.navigate(it)
//                }
//            }
        }


//        thread {
//
//        }
    }
}
