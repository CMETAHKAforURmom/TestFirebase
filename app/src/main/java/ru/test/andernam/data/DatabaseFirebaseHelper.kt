//package ru.test.andernam.data
//
//import ru.test.andernam.AppModule.provideCloudDatabase
//import ru.test.andernam.AppModule.provideDatabase
//
//class DatabaseFirebaseHelper {
//    val databaseLocal = provideDatabase()
//    val databaseCloud = provideCloudDatabase()
//
//    suspend fun getThisUser() {
//        if (databaseLocal.userPhone != null)
//            databaseLocal.localUserInfo = databaseCloud.downloadProfile(databaseLocal.userPhone!!)
//    }
//
//    suspend fun getUser(userId: String): UserInfo? {
//        var userDownload: UserInfo? = null
//        val result = databaseLocal.localUsersMessagingInfo.forEach {
//            userDownload = if (it.userId == userId)
//                it
//            else {
//                databaseCloud.downloadProfile(userId)
//            }
//        }
//        return userDownload
//    }
//
//    suspend fun sendMessage(message: String){
//
//    }
//}