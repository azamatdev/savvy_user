package uz.mymax.savvyenglish.di

import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uz.mymax.savvyenglish.BuildConfig
import uz.mymax.savvyenglish.network.SavvyApi
import java.util.concurrent.TimeUnit


//private const val BASE_URL: String = "http://192.168.0.100:8080/"
private const val BASE_URL: String = "http://95.217.160.86:8080/"

val networkModule = module {

    single<SavvyApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(SavvyApi::class.java)
    }
    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .build()
    }
    single<OkHttpClient> {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(1000, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(false)
//            .addInterceptor(ConnectivityInterceptor(get()))
            .addInterceptor { chain ->
                try {
                    val request = chain.request().newBuilder()
                    request.addHeader("Content-type", "application/json")
                    request.addHeader(
                        "Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaG9ocnVoIiwicm9sZXMiOlsiQURNSU4iXSwiaWF0IjoxNjA2MjM5NjExLCJleHAiOjE2MDYyODI4MTF9.9jl4c1EyawFLk2FRAdKtiUyjHbyWSNiEsGQi-MpJVPw"
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