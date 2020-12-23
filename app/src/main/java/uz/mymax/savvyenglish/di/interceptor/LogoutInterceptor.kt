package uz.mymax.savvyenglish.di.interceptor

import android.content.Context
import android.content.Intent
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONException
import uz.mymax.savvyenglish.network.LOGOUT


class LogoutInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val globJson = response.body!!.string()
        try {
            if (response.code
                == 403
            ) {
                context.sendBroadcast(Intent(LOGOUT))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
            .body(ResponseBody.create(response.body!!.contentType(), globJson)).build()
    }

}