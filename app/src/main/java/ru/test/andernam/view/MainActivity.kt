package ru.test.andernam.view

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.credentials.CredentialManager
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ru.test.andernam.AppModule.provideHomeImpl
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.view.components.screens.splash.SplashComposable
import ru.test.andernam.view.theme.TestFirebaseTheme
import ru.test.andernam.view.ui_parts.Scaffold.MainScaffold
import javax.inject.Inject

//  Поиск пользователей - анимация
//  КЕШИРОВАНИЕ В БД!!! Карта Юзер\сообщения!!! рекативное обновление\Выбор, откуда брать сообщения или подгрузка их в _localUserFlow  #ВЫПОЛНЕНО


//  Плохо работает первый вход(при том, иногда дважды)
//  Авторизация по номеру
//  Загрузка сообщений в БД и обратно
//  Долгая проверка пользователя - сделать флаг isAutorized в БД, потом уже проверять через сеть
//  Долгая загрузка изображения пользователя - хранение img в памяти телефона(БД, при возможности)
//  Динамическое хранение списка пользователей и сообщений в БД - старые удалять из БД, при долистывании до незагруженных данных - подгружать в StateFlow, но не в БД

//  Фичи - файлы в сообщениях; Беседы; Связывание номера и гуглАкк(merge ID или мультаккаунт);
//  Раздел настроек(цвета, выход, в т.ч. отдельный раздел для диалогов с возможностью удаления переписки как у одного, так и у обоих(для бесед - только у одного))
//  -> расширенная БД для диалогов(скорее всего, системные сообщения внутри диалога, например ActualTheme = Default)

//  Очень не скорые фичи - аудиоплеер(концепция взаимодействия пока не ясна)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var storage: DatabaseVariables

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TestFirebaseTheme {
                MainScaffold(navController = navController, storage)
            }
        }
    }
}
