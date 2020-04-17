package dev.rivu.mvijetpackcomposedemo

import android.app.Application
import dev.rivu.mvijetpackcomposedemo.moviesearch.data.injection.dataModule
import dev.rivu.mvijetpackcomposedemo.moviesearch.presentation.injection.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(applicationContext)
            // declare modules
            modules(dataModule, presentationModule)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //Plant crash/log reporting tool with Timber
        }
    }
}