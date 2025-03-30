package ru.test.andernam.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.test.andernam.data.Message
import ru.test.andernam.data.SelfUser
import ru.test.andernam.data.UserInfo
import ru.test.andernam.data.entities.DialogEntity
import ru.test.andernam.data.entities.MessageEntity
import ru.test.andernam.data.entities.SelfUserEntity
import ru.test.andernam.data.entities.UserInfoEntity
import ru.test.andernam.data.utilites.toSelfUser

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UserInfoEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUsers(users: List<UserInfoEntity>): List<Long>

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}

@Dao
interface SelfUserDao {

    @Query("SELECT * FROM self_user LIMIT 1")
    fun getSelfUser(): SelfUserEntity?

    @Transaction
    suspend fun getThisUser(dialogs: Map<UserInfo, List<Message>>): SelfUser {
        val selfUserEntity = getSelfUser() ?: throw IllegalStateException("Self User is not found")
        return selfUserEntity.toSelfUser(dialogs)
    }

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun updateSelfUser(selfUser: SelfUserEntity)

    @Query("DELETE FROM self_user")
    suspend fun clearSelfUser()
}

@Dao
interface DialogsDao {

    @Query("SELECT * FROM dialogs")
    suspend fun getDialogs(): List<DialogEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun updateDialogs(dialogs: List<DialogEntity>)

    @Query("DELETE FROM dialogs")
    suspend fun clearDialogs()
}

@Dao
interface MessagesDao{

    @Query("SELECT * FROM messages")
    suspend fun getMessages(): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun updateMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages")
    suspend fun clearMessages()
}