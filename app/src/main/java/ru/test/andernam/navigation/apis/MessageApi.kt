package ru.test.andernam.navigation.apis

interface MessageApi : FeatureApi {
    val messageRoute: String
    var hrefDialog: String
    var messageHref: String
}