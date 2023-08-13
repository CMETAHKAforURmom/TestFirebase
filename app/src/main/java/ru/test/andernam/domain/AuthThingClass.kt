package ru.test.andernam.domain

import android.content.ContentValues
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ru.test.andernam.auth
import ru.test.andernam.callbacks
import ru.test.andernam.contextActivity
import ru.test.andernam.database
import ru.test.andernam.isEntered
import ru.test.andernam.resendToken
import ru.test.andernam.reslover
import ru.test.andernam.storage
import ru.test.andernam.storedVerificationId
import ru.test.andernam.user


    fun start(){
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("ru")
        database = Firebase.firestore
        storage = Firebase.storage
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
                isEntered = true
                user = auth.currentUser
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(ContentValues.TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                }
//            else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                // reCAPTCHA verification attempted with null Activity
//            }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(ContentValues.TAG, "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }
    }

 fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(contextActivity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "signInWithCredential:success")

                user = task.result?.user
            } else {
                // Sign in failed, display a message and update the UI
                Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
                // Update UI
            }
        }
}