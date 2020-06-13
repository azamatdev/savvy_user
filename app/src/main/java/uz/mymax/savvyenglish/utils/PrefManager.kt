package uz.mymax.savvyenglish.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager {
    companion object{
        private const val TOKEN = "token"
        private const val SAVVY_ENGLISH = "savvy_english"

        private fun getInstance(context: Context) : SharedPreferences{
            return context.getSharedPreferences(SAVVY_ENGLISH, Context.MODE_PRIVATE)
        }
        fun saveToken(context: Context, token :String){
            getInstance(context).edit().putString(TOKEN, token).apply()
        }
        fun getToken(context: Context){
            getInstance(context).getString(TOKEN, "")
        }
    }
}