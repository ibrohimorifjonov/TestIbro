package uz.test.api.repository

import android.util.Log
import kotlinx.coroutines.flow.flow
import uz.test.api.dto.PostAccessToken
import uz.test.api.network.IbroApi
import uz.test.api.network.Resource
import uz.test.api.network.safeApiCall

class IBroRepository constructor(
   val api: IbroApi
) {
    fun postAccessToken(post:PostAccessToken)= flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.postAccessToken(post) })
    }

    fun getGeneralData(token:String)= flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getData(token) })
    }
}
