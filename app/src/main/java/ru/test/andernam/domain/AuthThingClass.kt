package ru.test.andernam.domain

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.test.andernam.view.components.Routes
import ru.test.andernam.view.components.navigateTo
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthThingClass @Inject constructor(val activity: Activity, val userLiveData: LiveUserData) {

    @Inject
    lateinit var dbRequestImpl: DataBaseRequestImpl

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var storedVerificationId: String
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var isCodeSend = false

//    fun setActivity(_activity: Activity) {
//        activity = _activity
//    }

//    fun isUserAuthBefore() : Boolean{
//        return if(FirebaseAuth.getInstance().currentUser != null){
//            user = FirebaseAuth.getInstance()?.currentUser
//            true
//        }else
//            false
//    }

    fun signOut() {
        auth.signOut()
        user = null
        userLiveData.changeAuthState(false)
//        navigateTo(Routes.Enter)
    }

    fun enterAcc (phone: String) = runBlocking {
        async {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun start (userLiveData: LiveUserData) {
        auth = FirebaseAuth.getInstance()
        println("Done")
        auth.setLanguageCode("ru")
        userLiveData.auth.postValue(auth)
        if (auth.currentUser != null) {
            userLiveData.user.postValue(auth.currentUser)
            userLiveData.changeAuthState(true)
            if (auth.currentUser?.phoneNumber != null) {
                userLiveData.DBRequest.postValue(dbRequestImpl)
//                UserDataClass.createDBrequestClass()
                userLiveData.phone.postValue(auth.currentUser?.phoneNumber)
                dbRequestImpl.downloadUserProfile(auth.currentUser?.phoneNumber.toString())
//                UserDataClass.rememberThisUser(user?.phoneNumber!!)
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
                user = auth.currentUser
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(ContentValues.TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(ContentValues.TAG, "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                isCodeSend = true
                resendToken = token
            }
        }

    }

    fun signInWithCode(code: String) {
        if (isCodeSend) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        }
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) = runBlocking {
        async {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithCredential:success")
                        navigateTo(Routes.Main)
                        user = task.result?.user
                        dbRequestImpl.downloadUserProfile(auth.currentUser?.phoneNumber.toString())
                        if (user != null)
                            if (user?.phoneNumber != null)
                                userLiveData.phone.postValue(auth.currentUser?.phoneNumber)
                    } else {
                        // Sign in failed, display a message and update the UI
                        when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> {}
                            is FirebaseTooManyRequestsException -> {}
                            is FirebaseAuthActionCodeException -> {}
                        }
                        Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                        // Update UI
                    }
                }
        }
    }

}