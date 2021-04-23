package uz.test.api

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uz.test.api.di.appModule
import uz.test.api.di.networkModule
import uz.test.api.di.viewModelsModule
import uz.test.api.network.IbroApi
import uz.test.api.repository.IBroRepository
import java.util.concurrent.TimeUnit


class IbroAplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        mContext=applicationContext
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(applicationContext)
            modules(listOf(appModule, networkModule, viewModelsModule))

        }
    }

    var mContext:Context?=null

    companion object{

        private const val BASE_URL:String="https://mp1.iprobonus.com/"
        fun createRepo(context: Context):IBroRepository{


                val clientBuilder=OkHttpClient.Builder()
                        .connectTimeout(3000, TimeUnit.SECONDS)
                        .readTimeout(3000, TimeUnit.SECONDS)
                        .writeTimeout(3000, TimeUnit.SECONDS)
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor{chain ->

                            try {
                                val request=chain.request().newBuilder()
                                request.addHeader("Content-type","application/json")
                                request.addHeader("AccessKey","891cf53c-01fc-4d74-a14c-592668b7a03c")

                                return@addInterceptor chain.proceed(request.build())
                            }catch (e:Throwable){

                            }
                            return@addInterceptor chain.proceed(chain.request())
                        }

                clientBuilder.addInterceptor(ChuckInterceptor(context))

            val retrofit= Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return IBroRepository(retrofit.create(IbroApi::class.java))
        }
    }
}