package ru.test.andernam

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

@Composable
fun Navigation() {
    navController = rememberNavController()

    NavHost(navController = navController as NavHostController, startDestination = Routes.Enter.route){
        composable(Routes.Enter.route){
            EnteredComp(navController)
        }
        composable(Routes.Wait.route){
            checkComp(navController)
        }
        composable(Routes.Main.route){
            MainComp(navController)
        }
    }
}

sealed class Routes(val route: String){
    object Enter: Routes("Start")
    object Wait: Routes("Wait")
    object Main: Routes("Main")
}


lateinit var navController: NavController
lateinit var auth: FirebaseAuth
var user: FirebaseUser? = null
lateinit var contextActivity: Activity
lateinit var otpId: String
lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
lateinit var storedVerificationId: String
lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
lateinit var database: FirebaseFirestore
lateinit var storage: FirebaseStorage
lateinit var reslover: ContentResolver
var isEntered = false