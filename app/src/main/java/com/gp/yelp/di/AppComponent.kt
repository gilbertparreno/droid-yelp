package com.gp.yelp.di

import android.app.Application
import com.google.android.libraries.places.api.net.PlacesClient
import com.gp.yelp.app.App
import com.gp.yelp.utils.SharedPreferenceUtil
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface AppComponent {
    fun inject(app: App)
    fun application(): Application
    fun retrofit(): Retrofit
    fun sharedPreferenceUtil() : SharedPreferenceUtil
    fun placesClient() : PlacesClient
}