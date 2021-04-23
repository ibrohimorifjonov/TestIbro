package uz.test.api.data
import android.content.Context
import android.content.SharedPreferences

private const val TOKEN="token"
private const val I_Bro="i_bro"



class PrefManager (
)
{
    companion object{
        private fun getInstance(context: Context):SharedPreferences{
            return context.getSharedPreferences(I_Bro,Context.MODE_PRIVATE)
        }

        fun getToken(context: Context):String {
            return getInstance(context).getString(TOKEN,"")!!
        }
    }
}

