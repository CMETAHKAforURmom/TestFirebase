package ru.test.andernam.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "self_user")
data class SelfUserEntity(
    @PrimaryKey var id: String,
    var name: String,
    var imageHref: String,
    var dialogs: String
)
