package ru.test.andernam.data

import ru.test.andernam.data.entities.DialogEntity
import ru.test.andernam.data.entities.MessageEntity

data class DialogData(
    val dialogs: List<DialogEntity>,
    val messages: List<MessageEntity>
)
