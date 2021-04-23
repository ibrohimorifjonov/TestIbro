package uz.test.api.network

import android.content.Context
import android.content.Intent
import com.readystatesoftware.chuck.internal.ui.MainActivity
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONException

class LogoutInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response =chain.proceed(chain.request())
        val globJson=response.body!!.string()
        try {
            if (response.code==401){
                val intent= Intent(context, MainActivity::class.java)
                intent.putExtra("isRegistered",false)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
        return response.newBuilder()
            .body(ResponseBody.create(response.body!!.contentType(),globJson)).build()
    }
}