package com.gp.yelp.screen.main

import com.gp.yelp.di.ActivityScope
import com.gp.yelp.di.AppComponent
import com.gp.yelp.viewModel.ViewModelFactoryModule
import com.gp.yelp.viewModel.MainViewModelModule
import dagger.Component

@ActivityScope
@Component(
    modules = [MainModule::class, ViewModelFactoryModule::class, MainViewModelModule::class],
    dependencies = [AppComponent::class]
)
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}