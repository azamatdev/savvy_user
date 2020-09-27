package uz.mymax.savvyenglish.di

import uz.mymax.savvyenglish.network.ConnectivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uz.mymax.savvyenglish.BuildConfig
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.response.ErrorResponse
import java.util.concurrent.TimeUnit


//private const val BASE_URL: String = "http://192.168.0.100:8080/"
private const val BASE_URL: String = "http://95.217.160.86:8080/"

val networkModule = module {

    single<SavvyApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(SavvyApi::class.java)
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
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
            .addInterceptor { chain ->
                try {
                    val request = chain.request().newBuilder()
                    request.addHeader("Content-type", "application/json")
                    request.addHeader(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaG9ocnVoIiwicm9sZXMiOlsiQURNSU4iXSwiaWF0IjoxNjAxMTkwOTI4LCJleHAiOjE2MDEyMzQxMjh9.9F-TFKi5ZwK8hOjBNpaoKgI8FHGo4wEzyTYaZPzltLo"
                    )
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