package ru.test.andernam.domain.impl

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.domain.api.AuthApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private var storedVerificationId: String? = null

@Singleton
class AuthImpl @Inject constructor(
    private val database: DatabaseVariables,
    private val credentialManager: CredentialManager
) : AuthApi {

    fun checkEnter(): Boolean {
        if (database.user != null)
            database.userUID = database.user?.uid
        return database.user != null
    }

    suspend fun signInWithGoogle(context: Context): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId("984303206594-vh67ja2iohsu8i0nskvnspmeik8jsqmt.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context, request)
                handleGoogleSignIn(result)
                Result.success(Unit)
            } catch (e: NoCredentialException) {
                Log.e("EnteredViewModel", "No Google accounts available: ${e.message}", e)
                Result.failure(e)
            } catch (e: GetCredentialException) {
                Log.e("EnteredViewModel", "Google Sign-In failed: ${e.type} - ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    private suspend fun handleGoogleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        when (credential) {
            is GoogleIdTokenCredential -> {
                val idToken = credential.idToken
                tryEnterCredential(idToken)
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    Log.d("AuthImpl", "Google ID Token received (from CustomCredential): $idToken")
                    tryEnterCredential(idToken)
                } else {
                    Log.e("AuthImpl", "Unexpected CustomCredential type: ${credential.type}")
                }
            }

            else -> {
                Log.e("AuthImpl", "Unexpected credential type: ${credential.javaClass.simpleName}")
            }
        }
    }

    suspend fun tryEnterCredential(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = database.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    database.userUID = database.auth.currentUser?.uid ?: ""
            }
    }

    private suspend fun checkAndCreateUser(user: FirebaseUser){

    }

    override fun sendSMS(phone: String, context: Context) {
//        Firebase.initialize(context = context)
//        Firebase.appCheck.installAppCheckProviderFactory(
//            PlayIntegrityAppCheckProviderFactory.getInstance(),
//        )
        database.auth.setLanguageCode("ru")
        val callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                database.user = database.auth.currentUser
                database.userUID = database.auth.currentUser?.uid ?: ""
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