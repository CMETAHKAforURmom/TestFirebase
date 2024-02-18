package ru.test.andernam.navigation.apis

interface HomeApi: FeatureApi {

    val homeRoute: String
    val profileRoute: String
    val messagesRoute: String
}