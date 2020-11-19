package uz.mymax.savvyenglish.network

import okhttp3.ResponseBody
import uz.mymax.savvyenglish.network.response.ErrorResponse
import java.io.IOException
import java.lang.Exception

sealed class NetworkState<out T : Any> {
    object Loading : NetworkState<Nothing>()
    data class Success<out T : Any>(val data: T) : NetworkState<T>()
    data class Error(val exception: Exception) : NetworkState<Nothing>()
    data class GenericError(val errorResponse: ErrorResponse) : NetworkState<Nothing>()
}

fun ResponseBody?.toException() = IOException(this?.string())