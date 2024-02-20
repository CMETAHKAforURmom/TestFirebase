package ru.test.andernam.domain.newest.impl

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.domain.newest.api.AuthApi
import java.util.concurrent.TimeUnit


private var storedVerificationId: String? = null

class AuthImpl(private val database: DatabaseVariables) : AuthApi {

//    var activity: Activity? = null

    override fun sendSMS(phone: String, context: Context) {
        val callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                database.user = database.auth.currentUser
                database.userPhone = database.user?.phoneNumber
            }

            override fun onVerificationFailed(exception: FirebaseException) {
            }

            override fun onCodeSent(
                verivicationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verivicationId
//                Log.i("Firebase auth", "ver id is ${storedVerificationId}")
            }
        }
        val options = PhoneAuthOptions.newBuilder(database.auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callBack)
            .setActivity(context as Activity)
            .build()
//        Log.i("Firebase auth", activity!!.toString())
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun returnSMS(code: String): Boolean {

        if (storedVerificationId != null) {
            val request = GlobalScope.async(Dispatchers.IO) {
                val task = database.auth.signInWithCredential(
                    PhoneAuthProvider.getCredential(
                        storedVerificationId!!,
                        code
                    )
                )
                    .addOnCompleteListener {
                        database.user = it.result?.user
                    }
                    .addOnFailureListener {
                        Log.e("Firebase auth", "error $it")
                    }
                task.await()
                return@async task.isSuccessful
            }
            return request.await()
        } else
            return false
    }
}