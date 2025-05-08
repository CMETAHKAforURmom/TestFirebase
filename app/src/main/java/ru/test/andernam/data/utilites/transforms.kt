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
        messages = opponentsList.toMutableMap()
    )

fun UserInfo.toEntity(selfUserId: String): UserInfoEntity = UserInfoEntity(
    opponentId = 0,
    selfUserId = selfUserId,
    userId = userId,
    name = userName.value,
    imageHref = userImageHref.value.toString()
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

fun Message.toEntity(): MessageEntity = MessageEntity(messageId = "${System.currentTimeMillis()}_${user}_$messageText", date = date, user = user, messageText = messageText, dialogId = dialogId)

fun MessageEntity.toMessage(): Message =
    Message(date = date, user = user, messageText = messageText, dialogId = dialogId)

fun DialogData.toMap(users: List<UserInfoEntity>): Map<UserInfo, List<Message>> {

    var output = emptyMap<UserInfo, List<Message>>()
    var localMessagesList = emptyList<Message>()
    users.forEach { user ->
        var findingDialog: DialogEntity? = null

        this.dialogs.forEach { dialog ->
            if (dialog.opponentId == user.opponentId) {
                findingDialog = dialog
            }
        }

        this.messages.forEach { message ->

            if (message.dialogId == findingDialog?.dialogId)
                localMessagesList += message.toMessage()
        }
        if(findingDialog != null)
            output += Pair(user.toUserInfo(listOf(findingDialog.dialogId)), localMessagesList)
    }
    return output
}

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