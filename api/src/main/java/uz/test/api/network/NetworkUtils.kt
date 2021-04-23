package uz.test.api.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

import retrofit2.Response
import uz.test.api.dto.ErrorResponse
import uz.test.api.exeption.NoConnectivityException
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket

suspend fun <T:Any> safeApiCall(
    apiCall:suspend()-> Response<T>
):Resource<T>{
    try {
        val response=apiCall()
        Log.d("response",response.toString())
        if(response.isSuccessful&&response.body()!=null){
            return Resource.Success<T>(response.body() as T)
        }else{
            if(response.errorBody()!=null){
                val errorResponse= ErrorResponse(jsonResponse= JSONObject(response.errorBody()!!.string()))
                return Resource.GenericError(errorResponse)
            }else{
                val errorResponse= ErrorResponse(jsonResponse= JSONObject(response.errorBody()!!.string()))
                return Resource.GenericError(errorResponse)
            }
        }
    }catch (throwable:Throwable){
        Log.d("ErrorTag",throwable.message.toString())
        when(throwable){
            is ConnectException,is NoConnectivityException ->{
                return Resource.Error(NoConnectivityException())
            }
            is HttpException ->{
                val errorResponse:ErrorResponse=throwable.response()?.body()as ErrorResponse
                return Resource.GenericError(errorResponse)
            }
            is IOException ->{
                return Resource.Error(Exception("IOException: "+throwable.message))
            }else->{
            return Resource.Error(Exception(throwable.message))
        }
        }
    }
}
fun Context.isNetworkAvailable():Boolean{
    var result=false
    val cm=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result=hasTransport((NetworkCapabilities.TRANSPORT_CELLULAR))
            }
        }
    }
    else{
        cm?.run { cm.activeNetworkInfo?.run {
            result=type== ConnectivityManager.TYPE_WIFI||type== ConnectivityManager.TYPE_MOBILE
        } }
    }
    return result
}
suspend fun Context.isNetworkConnectedSuspend():Boolean{
    return withContext(Dispatchers.IO){
        try {
            val timeoutsMs=1500
            val socket= Socket()
            val socketAddress= InetSocketAddress("8.8.8.8",53)
            socket.connect(socketAddress,timeoutsMs)
            socket.close()
            true
        }catch (e: IOException){
            false
        }
    }
}