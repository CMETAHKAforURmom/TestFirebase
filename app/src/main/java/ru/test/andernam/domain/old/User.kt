package ru.test.andernam.domain.old

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object User {

    lateinit var idClient: String
    var dialogList: MutableList<Array<String>>? = null

}