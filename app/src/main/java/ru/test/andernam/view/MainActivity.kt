package ru.test.andernam.view

//import androidx.hilt.navigation.compose.hiltViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.test.andernam.AppModule.provideAuthImpl
import ru.test.andernam.view.theme.TestFirebaseTheme

//lateinit var userClass: DownloadUploadHelp

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
//
//    @Inject
//    lateinit var navigator : Navigator
//
//    @Inject
//    lateinit var userClass: UserClass
//
//    @Inject
//    lateinit var authThingClass: AuthThingClass
//
//    private var userLiveData: LiveUserData = provideUserLiveData()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authTry = provideAuthImpl()
        authTry.activity = this
        setContent {
            val navController = rememberNavController()
            TestFirebaseTheme {
//                navigator.Navigation(navController)
//                AppNavGraph(navController = navController)
                authTry.sendSMS("+79515817958", this)
//                MainScaffold(navController = navController)
            }
//            lifecycleScope.launchWhenCreated {
//                userLiveData.isAuthPassed.collectLatest {
//                    if(it)
//                        navController.navigate("messages")
//                }
//            }
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
