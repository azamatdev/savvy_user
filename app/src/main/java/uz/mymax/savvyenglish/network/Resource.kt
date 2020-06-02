package uz.mymax.savvyenglish.network

import okhttp3.ResponseBody
import java.io.IOException
import java.lang.Exception

sealed class Resource<out T: Any>{
    object Loading : Resource<Nothing>()
    object RequireLogin : Resource<Nothing>()
    data class Success<out T : Any>(val data : T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()
}

fun ResponseBody?.toException() = IOException(this?.string())