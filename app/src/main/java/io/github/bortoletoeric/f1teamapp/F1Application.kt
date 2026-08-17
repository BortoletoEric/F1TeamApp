package io.github.bortoletoeric.f1teamapp

import android.app.Application
import io.github.bortoletoeric.f1teamapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class F1Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@F1Application)
            modules(appModule)
        }
    }
}
