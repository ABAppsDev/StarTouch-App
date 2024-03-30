package org.abapps.project

import android.app.Application
import di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StarTouchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StarTouchApp)
            androidLogger(
                level = Level.INFO
            )
            modules(AppModule)
            createEagerInstances()
        }
    }
}