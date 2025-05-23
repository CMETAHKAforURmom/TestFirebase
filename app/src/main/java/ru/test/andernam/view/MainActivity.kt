package ru.test.andernam.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.test.andernam.data.DatabaseVariables
import ru.test.andernam.view.theme.TestFirebaseTheme
import ru.test.andernam.view.ui_parts.Scaffold.MainScaffold
import javax.inject.Inject


//  Разобраться с псевдо-модульностью в MessageList - перенести Top & Bottom Bar's в контент(т.к. нет переиспользования)
//  Убрать бинарные флаги в Scaffold, заменив их State'ом ^
//  Поиск пользователей - анимация; тап по пользователю - или сообщения, или профиль если картинка; отдельный экран для поиска
//  Авторизация по номеру

//  Долгая загрузка изображения пользователя - хранение img в памяти телефона(БД, при возможности)
//  Динамическое хранение списка пользователей и сообщений в БД - старые удалять из БД, при долистывании до незагруженных данных - подгружать в StateFlow, но не в БД

//  Сломались новые диалоги #ВЫПОЛНЕНО
//  КЕШИРОВАНИЕ В БД!!! Карта Юзер\сообщения!!! рекативное обновление\Выбор, откуда брать сообщения или подгрузка их в _localUserFlow  #ВЫПОЛНЕНО
//  Плохо работает первый вход(при том, иногда дважды) #ВЫПОЛНЕНО
//  Загрузка сообщений в БД и обратно #ВЫПОЛНЕНО
//  Долгая проверка пользователя - сделать флаг isAutorized в БД, потом уже проверять через сеть #ВЫПОЛНЕНО

//  Создать утилиту OnConnectionLost, OnConnectionRestored
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
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch{storage.getThisUser()
        storage.getAllUsers()}
        window.setWindowAnimations(0)
        display.supportedModes.find { it.refreshRate == 60f }?.let {
            val params = window.attributes
            params.preferredDisplayModeId = it.modeId
            window.attributes = params
        }
        setContent {
            val navController = rememberNavController()
            TestFirebaseTheme{
                MainScaffold(navController = navController)
            }
        }
    }
}
