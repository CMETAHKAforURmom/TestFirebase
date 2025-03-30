package ru.test.andernam.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.test.andernam.data.entities.DialogEntity
import ru.test.andernam.data.entities.MessageEntity
import ru.test.andernam.data.entities.SelfUserEntity
import ru.test.andernam.data.entities.UserInfoEntity

@Database(
    entities = [
        SelfUserEntity::class,
        UserInfoEntity::class,
        DialogEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun selfUserDao(): SelfUserDao
    abstract fun dialogDao(): DialogsDao
    abstract fun messageDao(): MessagesDao
}