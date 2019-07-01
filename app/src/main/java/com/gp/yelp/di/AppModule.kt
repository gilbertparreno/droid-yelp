package com.gp.yelp.di

import android.app.Application
import com.gp.yelp.utils.SharedPreferenceUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Singleton
    @Provides
    fun providesApplication() : Application {
        return application
    }

    @Singleton
    @Provides
    fun providesSharedPreferenceUtil(application: Application) : SharedPreferenceUtil {
        return SharedPreferenceUtil(application.applicationContext)
    }
}