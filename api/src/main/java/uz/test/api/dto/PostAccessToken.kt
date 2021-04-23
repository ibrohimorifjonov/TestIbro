package uz.test.api.dto

data class PostAccessToken(
    val idClient: String,
    val accessToken: String,
    val paramName: String,
    val paramValue: String,
    val latitude: Int,
    val longitude: Int,
    val sourceQuery: Int
)