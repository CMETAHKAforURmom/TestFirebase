package ru.test.andernam.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    foreignKeys = [ForeignKey(
        entity = SelfUserEntity::class,
        parentColumns = ["id"],
        childColumns = ["selfUserId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true) val opponentId: Long,
    val selfUserId: String,
    val userId: String,
    val name: String,
    val imageHref: String
)