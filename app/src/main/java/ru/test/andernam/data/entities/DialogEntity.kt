package ru.test.andernam.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "dialogs",
    foreignKeys = [
        ForeignKey(
            entity = SelfUserEntity::class,
            parentColumns = ["id"],
            childColumns = ["selfUserId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = UserInfoEntity::class,
            parentColumns = ["opponentId"],
            childColumns = ["opponentId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index(value = ["selfUserId", "opponentId"])]
)
data class DialogEntity(
    @PrimaryKey val dialogId: String,
    val selfUserId: String,
    val opponentId: Long
)
