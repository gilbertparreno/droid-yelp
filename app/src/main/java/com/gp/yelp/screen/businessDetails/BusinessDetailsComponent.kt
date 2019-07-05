package com.gp.yelp.screen.main

import com.gp.yelp.di.AppComponent
import com.gp.yelp.di.FragmentScope
import com.gp.yelp.viewModel.BusinessDetailsViewModelModule
import com.gp.yelp.viewModel.ViewModelFactoryModule
import dagger.Component

@FragmentScope
@Component(
    modules = [BusinessDetailsModule::class, ViewModelFactoryModule::class, BusinessDetailsViewModelModule::class],
    dependencies = [AppComponent::class, MainView::class]
)
interface BusinessDetailsComponent {
    fun inject(businessDetailsFragment: BusinessDetailsFragment)
}