//package ru.test.andernam.old.interfaces
//
//import android.app.Activity
//import android.content.ContentValues
//import android.util.Log
//import com.google.firebase.FirebaseException
//import com.google.firebase.FirebaseTooManyRequestsException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthActionCodeException
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthOptions
//import com.google.firebase.auth.PhoneAuthProvider
//import kotlinx.coroutines.DelicateCoroutinesApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.async
//import kotlinx.coroutines.runBlocking
//import ru.test.andernam.old.interfaces.repository.LiveUserData
//import java.util.concurrent.TimeUnit
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class AuthThingClass @Inject constructor(val activity: Activity) {
//
////    val dbRequestImpl: DBFirstStep = DBFirstStep()
//
//    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
//    private lateinit var storedVerificationId: String
//    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
//            user = auth.currentUser
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            Log.w(ContentValues.TAG, "onVerificationFailed", e)
//            if (e is FirebaseAuthInvalidCredentialsException) {
//            } else if (e is FirebaseTooManyRequestsException) {
//            }
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            token: PhoneAuthProvider.ForceResendingToken,
//        ) {
//            Log.d(ContentValues.TAG, "onCodeSent:$verificationId")
//            storedVerificationId = verificationId
//            isCodeSend = true
//            resendToken = token
//        }
//    }
//    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private var user: FirebaseUser? = null
//    private var isCodeSend = false
//
////    fun setActivity(_activity: Activity) {
////        activity = _activity
////    }
//
////    fun isUserAuthBefore() : Boolean{
////        return if(FirebaseAuth.getInstance().currentUser != null){
////            user = FirebaseAuth.getInstance()?.currentUser
////            true
////        }else
////            false
////    }
//
//    fun signOut() {
//        auth.signOut()
//        user = null
////        userLiveData.changeAuthState(false)
////        navigateTo(Routes.Enter)
//    }
//
//    fun enterAcc (phone: String) = runBlocking {
//        async {
//            val options = PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phone)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(activity)
//                .setCallbacks(callbacks)
//                .build()
//            PhoneAuthProvider.verifyPhoneNumber(options)
//        }
//    }
//    suspend fun signInWithCode(code: String): Boolean {
//        return if (isCodeSend) {
//            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
//            signInWithPhoneAuthCredential(credential)
//        }else
//            false
//    }
//    @OptIn(DelicateCoroutinesApi::class)
//    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential):Boolean {
//        val request = GlobalScope.async(Dispatchers.IO) {
//            var result = false
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener(activity) { task ->
//                    if (task.isSuccessful) {
//                        user = task.result?.user
//                        dbRequestImpl.downloadUserProfile(
//                            auth.currentUser?.phoneNumber.toString(),
//                            true
//                        )
//                        result = true
////                        provideUserLiveData().setAuthPassed()
//                        if (user != null)
//                            if (user?.phoneNumber != null) {
//
//                            }
//                    } else {
//                        result = false
//                        when (task.exception) {
//                            is FirebaseAuthInvalidCredentialsException -> {}
//                            is FirebaseTooManyRequestsException -> {}
//                            is FirebaseAuthActionCodeException -> {}
//                        }
//
//                    }
//                }
//            return@async result
//        }
//        return request.await()
//    }
//
//
//    fun start (userLiveData: LiveUserData) {
//        auth = FirebaseAuth.getInstance()
//        println("Done")
//        auth.setLanguageCode("ru")
////        userLiveData.auth.postValue(auth)
//        if (auth.currentUser != null) {
////            userLiveData.user.postValue(auth.currentUser)
////            userLiveData.changeAuthState(true)
//            if (auth.currentUser?.phoneNumber != null) {
////                userLiveData.DBRequest.postValue(dbRequestImpl)
////                UserDataClass.createDBrequestClass()
////                userLiveData.phone.postValue(auth.currentUser?.phoneNumber)
//                dbRequestImpl.downloadUserProfile(auth.currentUser?.phoneNumber.toString(), true)
////                UserDataClass.rememberThisUser(user?.phoneNumber!!)
//            }
//        }
//
//
//
//    }
//
//
//
//
//}