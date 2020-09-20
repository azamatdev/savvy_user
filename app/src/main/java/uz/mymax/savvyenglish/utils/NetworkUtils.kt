package uz.mymax.savvyenglish.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import uz.mymax.savvyenglish.exceptions.EmptyBodyException
import uz.mymax.savvyenglish.exceptions.NoConnectivityException
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.response.ErrorResponse
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket


suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> Response<T>
): Resource<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            return Resource.Success<T>(response.body() as T)
        } else {
            if (response.errorBody() != null) {
                val jsonParser = JsonParser()
                val jsonElement = jsonParser.parse(response.errorBody()!!.string())
                val errorResponse = Gson().fromJson(
                    jsonElement,
                    ErrorResponse::class.java
                )
                return Resource.GenericError(errorResponse)
            } else
                return Resource.GenericError(ErrorResponse(message = "Unknown error"))
        }
    } catch (throwable: Throwable) {
        Log.d("ErrorTag", throwable.message.toString())
        when (throwable) {
            is ConnectException,
            is NoConnectivityException -> {
                return Resource.Error(NoConnectivityException())
            }
            is HttpException -> {
                val errorResponse: ErrorResponse = throwable.response()?.body() as ErrorResponse
                return Resource.GenericError(errorResponse)
            }
            is IOException -> {
                return Resource.Error(Exception("IOException: " + throwable.message))
            }
            else -> {
                return Resource.Error(Exception(throwable.message))
            }
        }
    }
}


/**
 * Checks the availability of network connection not the actual internet connection
 * After Marshmellow new Connectivity Api is used  called networkCapabilities.
 *
 */
fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                result =
                    type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE
            }
        }
    }
    return result
}


suspend fun Context.isNetworkConnectedSuspend(): Boolean {
    // Dispatchers.Main
    return withContext(Dispatchers.IO) {
        try {
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            true
        } catch (e: IOException) {
            false
        }
    }
}
