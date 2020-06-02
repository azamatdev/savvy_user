package uz.mymax.savvyenglish.di

import uz.mymax.savvyenglish.network.ConnectivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uz.mymax.savvyenglish.BuildConfig
import uz.mymax.savvyenglish.network.SavvyApi
import java.util.concurrent.TimeUnit


//private const val BASE_URL: String = "http://192.168.0.100:8080/"
private const val BASE_URL: String = "https://safe-reaches-77218.herokuapp.com"

val networkModule = module {

    single<SavvyApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(SavvyApi::class.java)
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    single<OkHttpClient> {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(1000, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(ConnectivityInterceptor(get()))
            //If token is added then add the auth-token for every request
            .addInterceptor { chain ->
                try {
                    val request = chain.request().newBuilder()
                    request.addHeader("Content-type", "application/json")
                    return@addInterceptor chain.proceed(request.build())
                } catch (e: Throwable) {

                }
                return@addInterceptor chain.proceed(chain.request())
            }
        //If debugged version, network request debugger added
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(ChuckInterceptor(get()))
        }
        clientBuilder.build()
    }


}