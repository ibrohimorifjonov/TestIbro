package uz.test.api.dto

data class ResultAccessToken(
    val result: ResultAccessTokenBody,
    val accessToken: String
)
data class ResultAccessTokenBody(
    val status:Int,
    val messageDev:String?=null,
    val message:String,
    val codeResult:Int,
    val duration:Double=0.0,
    val idLog:String
)