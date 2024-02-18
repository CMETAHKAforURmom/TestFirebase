package ru.test.andernam.data

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DatabaseVariables {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var user: FirebaseUser? = null
    var userPhone: String? = null
    var activity: Activity? = null
    var storedVerificationId: String? = null

//    fun getActivity(){
//        MainActivity()
//    }
}