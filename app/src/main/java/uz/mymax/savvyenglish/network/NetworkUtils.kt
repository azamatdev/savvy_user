package uz.mymax.savvyenglish.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import uz.mymax.savvyenglish.exceptions.NoConnectivityException
import uz.mymax.savvyenglish.network.response.ErrorResponse
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket


suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> Response<T>
): NetworkState<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            return NetworkState.Success<T>(response.body() as T)
        } else
            if (response.isSuccessful) {
                return NetworkState.Success<T>("" as T)
            } else {
                if (response.errorBody() != null) {
                    val json = JSONObject(response.errorBody()!!.string())
                    val jsonParser = JsonParser()
                    val jsonElement = jsonParser.parse(response.errorBody()!!.string())
                    val errorResponse = Gson().fromJson(
                        jsonElement,
                        ErrorResponse::class.java
                    )
                    errorResponse.message = errorResponse.error
                    if(json.has("error"))
                        errorResponse.message = errorResponse.error
                    return NetworkState.GenericError(errorResponse)
                } else
                    return NetworkState.GenericError(ErrorResponse(message = "Unknown error"))
            }
    } catch (throwable: Throwable) {
        Log.d("ErrorTag", throwable.message.toString())
        when (throwable) {
            is ConnectException,
            is NoConnectivityException -> {
                return NetworkState.Error(NoConnectivityException())
            }
            is HttpException -> {
                val errorResponse: ErrorResponse = throwable.response()?.body() as ErrorResponse
                return NetworkState.GenericError(errorResponse)
            }
            is IOException -> {
                return NetworkState.Error(Exception("IOException: " + throwable.message))
            }
            else -> {
                return NetworkState.Error(Exception(throwable.message))
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
