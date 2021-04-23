package uz.test.api.network

import retrofit2.Response
import retrofit2.http.*
import uz.test.api.dto.*

interface IbroApi {
    @POST("api/v3/clients/accesstoken")
    suspend fun postAccessToken(@Body post:PostAccessToken):Response<ResultAccessToken>

    @GET("api/v3/ibonus/generalinfo/{token}")
    suspend fun getData(@Path("token")token:String):Response<GeneralInfo>
}