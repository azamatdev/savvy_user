package uz.mymax.savvyenglish.data

import android.content.Context
import android.content.SharedPreferences


private val SAVVY = "savvy"
private val KEY_TOKEN = "token"

private fun getInstance(context: Context): SharedPreferences {
    return context.getSharedPreferences(SAVVY, Context.MODE_PRIVATE)
}

fun Context.saveToken(token: String) {
    getInstance(this).edit().putString(KEY_TOKEN, token).apply()
}

fun Context.getToken(): String {
    return getInstance(this).getString(KEY_TOKEN, "") ?: ""
}