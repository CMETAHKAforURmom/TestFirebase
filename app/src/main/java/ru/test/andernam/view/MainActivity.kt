package ru.test.andernam.view

//import androidx.hilt.navigation.compose.hiltViewModel
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.AndroidEntryPoint
import ru.test.andernam.view.theme.TestFirebaseTheme
import ru.test.andernam.view.ui_parts.Scaffold.MainScaffold
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        var mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, 9001)
//        var auth = FirebaseAuth.getInstance()
//
//        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                // Сюда попадет если пользователь уже имеет активную сессию авторизации
//                auth.signInWithCredential(credential)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            // Авторизация успешна
//                        } else {
//                            // Обработка ошибок
//                        }
//                    }
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                // Обработка ошибок
//            }
//
//
//        }
//        // Код для отправки SMS с кодом подтверждения
//        val phoneNumber = "+79515817958"
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(callbacks)
//            .build()
//
//        PhoneAuthProvider.verifyPhoneNumber(options)


        setContent {
            val navController = rememberNavController()
            TestFirebaseTheme {
                MainScaffold(navController = navController)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.i("Google auth", "Success")
                // Авторизация успешна, обрабатываем данные аккаунта
            } catch (e: ApiException) {
                Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }
    init {
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
    }
}
