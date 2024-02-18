package ru.test.andernam.view.components

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Navigator @Inject constructor() {


//    @Inject
//    lateinit var blogComposable: BlogComposable


    var isNavigatorAsked = false
//    var defaultDestination = Routes.Main.route

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    @Composable
//    fun Navigation(navController: NavHostController) {
//        val localContext = LocalContext.current
//        isNavigatorAsked = true
//        setDefController(navController)
//        NavHost(navController = navController, startDestination = localContext.getString(R.string.start)) {
//            composable(localContext.getString(R.string.start)) {
////                EnteredComp()
//                isShowHelper.value = false
//            }
//            composable(localContext.getString(R.string.profile)) {
//                MainComp()
//                isShowHelper.value = true
//                isMessageShowHelper.value = false
//            }
//            composable(localContext.getString(R.string.messages)) {
////                blogComposable.BlogComp()
//                isShowHelper.value = true
//                isMessageShowHelper.value = false
//            }
//            composable(localContext.getString(R.string.message_screen)) {
//                SendMessageScreen()
//                isMessageShowHelper.value = true
//            }
//        }
//    }
}
//
//sealed class Routes(val route: String){
//    object Enter: Routes("Start")
//    object Main: Routes("Main")
//    object Blog: Routes("Blog")
//    object Message: Routes("Message")
//    object Back: Routes("Back")
//    object Def: Routes("Start")
//}