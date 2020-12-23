package uz.mymax.savvyenglish

import android.os.Handler
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import uz.mymax.savvyenglish.data.setAdmin
import uz.mymax.savvyenglish.di.appModule
import uz.mymax.savvyenglish.di.networkModule
import uz.mymax.savvyenglish.di.viewModelsModule

class SavvyApplication : MultiDexApplication() {

    companion object {
        @Volatile
        var applicationHandler: Handler? = null
    }
    override fun onCreate() {
        super.onCreate()
        applicationHandler =
            Handler(applicationContext.mainLooper)

        MultiDex.install(this)
        setAdmin(BuildConfig.APPLICATION_ID.contains("admin"))
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(applicationContext)
            modules(listOf(appModule, networkModule, viewModelsModule))
        }
    }
}