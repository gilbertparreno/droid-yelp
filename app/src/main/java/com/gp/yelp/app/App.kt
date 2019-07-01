package com.gp.yelp.app

import android.app.Application
import com.gp.yelp.BuildConfig
import com.gp.yelp.di.AppComponent
import com.gp.yelp.di.AppModule
import com.gp.yelp.di.DaggerAppComponent
import com.gp.yelp.di.NetworkModule
import timber.log.Timber


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerAppComponent.builder()
            .networkModule(
                NetworkModule(
                    "https://api.yelp.com/v3/",
                    "7EQyQ2QZhMRoKBna6YApSqj4iC0IknqAS8coqGmvMdcp2qXNkIfh1Vlm3oUAUNaLyV4KsO8Ld7ItTAmhJvsQiFMWZg6qEZz7qBuLIHMlselO00_ds9vsq9uQwb4ZXXYx", BuildConfig . DEBUG
                )
            )
            .appModule(AppModule(this))
            .build()
    }
}