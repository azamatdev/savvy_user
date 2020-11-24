package uz.mymax.savvyenglish.data

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment


private val SAVVY = "savvy"
private val KEY_TOKEN = "token"
private val IS_LOGGED_IN = "is_logged_in"
private val IS_ADMIN = "is_admin"

private fun getInstance(context: Context): SharedPreferences {
    return context.getSharedPreferences(SAVVY, Context.MODE_PRIVATE)
}

fun Context.saveToken(token: String) {
    getInstance(this).edit().putString(KEY_TOKEN, token).apply()
}

fun Context.getToken(): String {
    return getInstance(this).getString(KEY_TOKEN, "") ?: ""
}

fun Context.setLoggedIn(isLoggedIn: Boolean) {
    getInstance(this).edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
}

fun Context.isLoggedIn(): Boolean {
    return getInstance(this).getBoolean(IS_LOGGED_IN, false)
}

fun Context.setAdmin(isAdmin: Boolean) {
    getInstance(this).edit().putBoolean(IS_ADMIN, isAdmin).apply()
}

fun Context.isAdmin(): Boolean {
    return getInstance(this).getBoolean(IS_ADMIN, false)
}

