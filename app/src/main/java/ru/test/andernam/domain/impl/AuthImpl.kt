package ru.test.andernam.domain.impl

//import ru.test.andernam.view.context
import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.domain.api.AuthApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private var storedVerificationId: String? = null

@Singleton
class AuthImpl @Inject constructor(private val database: DatabaseVariables) : AuthApi {


    fun checkEnter(): Boolean {
        if (database.user != null)
            database.userPhone = database.user?.phoneNumber
        return database.user != null
    }

    override fun sendSMS(phone: String, context: Context) {
        Firebase.initialize(context = context)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        database.auth.setLanguageCode("ru")
        val callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                database.user = database.auth.currentUser
                database.userPhone = database.user?.phoneNumber
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.i("Login firebase", exception.toString())
            }

            override fun onCodeSent(
                verivicationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verivicationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(database.auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callBack)
            .setActivity(context as Activity)
            .build()
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