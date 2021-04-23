package uz.test.api.di

import android.content.Context
import android.util.Base64
import com.readystatesoftware.chuck.BuildConfig
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uz.test.api.data.PrefManager

import uz.test.api.dto.ErrorResponse
import uz.test.api.network.IbroApi
import uz.test.api.network.LogoutInterceptor
import java.util.concurrent.TimeUnit

private const val BASE_URL:String="https://mp1.iprobonus.com/"

val networkModule= module {
    single<IbroApi>{
        val retrofit=get<Retrofit>()
        retrofit.create(IbroApi::class.java)
    }
    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory((MoshiConverterFactory.create(get())))
            .build()
    }
    single<OkHttpClient> {
        val clientBuilder=OkHttpClient.Builder()
            .connectTimeout(3000, TimeUnit.SECONDS)
            .readTimeout(3000, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .addInterceptor(LogoutInterceptor(context = get()))
            .addInterceptor{chain ->
                val token:String=PrefManager.getToken(get())
                try {
                    val request=chain.request().newBuilder()
                    request.addHeader("Content-type","application/json")
                    request.addHeader("X-Requested-Width","XMLHttpRequest")
                    request.addHeader("AccessKey","891cf53c-01fc-4d74-a14c-592668b7a03c")
                    if (token!=""){
                        val credentials:String="$token:"
                        val base64EncodedCredentials:String=
                            Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                        request.addHeader("Authorization","Basic $base64EncodedCredentials")
                    }
                    return@addInterceptor chain.proceed(request.build())
                }catch (e:Throwable){

                }
                return@addInterceptor chain.proceed(chain.request())
            }

            clientBuilder.addInterceptor(ChuckInterceptor(get()))

        clientBuilder.build()
    }
    factory <Converter<ResponseBody, ErrorResponse>>{
        get<Retrofit>().responseBodyConverter(
            ErrorResponse::class.java,
            arrayOfNulls<Annotation>(0)
        )
    }

}