package ru.test.andernam.data.entities

import androidx.room.*

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = DialogEntity::class,
            parentColumns = ["dialogId"],
            childColumns = ["dialogId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["dialogId"])]
)
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val date: String,
    val user: String,
    val messageText: String,
    val dialogId: String
)