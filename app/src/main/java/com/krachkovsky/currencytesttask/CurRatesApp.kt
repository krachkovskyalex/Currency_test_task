package com.krachkovsky.currencytesttask

import android.app.Application
import com.krachkovsky.currencytesttask.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CurRatesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@CurRatesApp)
            modules(appModule)
        }
    }
}