package ru.test.andernam.data.utilites

import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import ru.test.andernam.data.DialogData
import ru.test.andernam.data.Message
import ru.test.andernam.data.SelfUser
import ru.test.andernam.data.UserInfo
import ru.test.andernam.data.entities.DialogEntity
import ru.test.andernam.data.entities.MessageEntity
import ru.test.andernam.data.entities.SelfUserEntity
import ru.test.andernam.data.entities.UserInfoEntity

fun transformDialogsToList(dialogs: String): List<String> = dialogs.split(";").toList()

fun transformListToDialogs(dialogsList: List<String>): String = dialogsList.joinToString(";")


fun SelfUserEntity.toSelfUser(opponentsList: Map<UserInfo, List<Message>>): SelfUser =
    SelfUser(
        userId = id,
        userName = mutableStateOf(name),
        userImageHref = mutableStateOf(imageHref.toUri()),
        dialogs = transformDialogsToList(dialogs).toMutableList(),
        messages = opponentsList
    )

fun UserInfoEntity.toUserInfo(dialogsList: List<String>): UserInfo =
    UserInfo(
        userId = userId,
        userName = mutableStateOf(name),
        userImageHref = mutableStateOf(imageHref.toUri()),
        dialogsList = dialogsList.toMutableList()
    )

fun SelfUser.toEntity(): SelfUserEntity =
    SelfUserEntity(
        id = userId,
        name = userName.value,
        dialogs = transformListToDialogs(dialogs),
        imageHref = userImageHref.value.toString()
    )

fun MessageEntity.toMessage(): Message =
    Message(date = date, user = user, messageText = messageText)


fun SelfUser.toDialogData(): DialogData {
    val dialogs = mutableListOf<DialogEntity>()
    val messages = mutableListOf<MessageEntity>()

    for ((opponent, messageList) in this.messages) {
        val dialogHref = opponent.dialogsList
            .firstOrNull { it.contains(userId) }
            ?.split("|")
            ?.getOrNull(1)
            ?: continue
        val dialogEntity = DialogEntity(
            dialogId = dialogHref,
            selfUserId = this.userId,
            opponentId = 0
        )
        dialogs.add(dialogEntity)

        val messageEntities = messageList.map { message ->
            MessageEntity(
                messageId = "${System.currentTimeMillis()}_${message.user}_${message.messageText}",
                dialogId = dialogHref,
                date = message.date,
                user = message.user,
                messageText = message.messageText
            )
        }
        messages.addAll(messageEntities)
    }
    return DialogData(dialogs, messages)
}