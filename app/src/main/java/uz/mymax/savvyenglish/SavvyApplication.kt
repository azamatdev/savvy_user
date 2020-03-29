package uz.mymax.savvyenglish

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class SavvyApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}