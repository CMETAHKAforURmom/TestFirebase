package ru.test.andernam.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.view.theme.TestFirebaseTheme
import ru.test.andernam.view.ui_parts.Scaffold.MainScaffold
import javax.inject.Inject

//  Поиск пользователей??
//  КЕШИРОВАНИЕ В БД!!!
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
