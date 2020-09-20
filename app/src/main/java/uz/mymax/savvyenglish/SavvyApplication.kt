package uz.mymax.savvyenglish

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.mymax.savvyenglish.di.appModule
import uz.mymax.savvyenglish.di.networkModule
import uz.mymax.savvyenglish.di.viewModelsModule

class SavvyApplication : MultiDexApplication() {

    companion object{
        @Volatile
        lateinit var context : Context
    }
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        context = applicationContext
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(listOf(appModule, networkModule, viewModelsModule))
        }
    }
}