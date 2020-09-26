package uz.mymax.savvyenglish.network

import uz.mymax.savvyenglish.exceptions.NoConnectivityException
import android.content.Context

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class ConnectivityInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //1 Check if the network is available

        if(!context.isNetworkAvailable()) {
            throw NoConnectivityException()
        }
        //2. If Network available check the real status of the internet.
        //It will request google host, to get response
        runBlocking {
            if (!context.isNetworkConnectedSuspend()) {
                throw NoConnectivityException()
            }
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}



