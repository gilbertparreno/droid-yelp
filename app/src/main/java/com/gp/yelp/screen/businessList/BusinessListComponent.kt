package com.gp.yelp.screen.main

import com.gp.yelp.di.ActivityScope
import com.gp.yelp.di.AppComponent
import com.gp.yelp.di.FragmentScope
import com.gp.yelp.viewModel.BusinessListViewModelModule
import com.gp.yelp.viewModel.ViewModelFactoryModule
import dagger.Component

@FragmentScope
@Component(
    modules = [BusinessListModule::class, ViewModelFactoryModule::class, BusinessListViewModelModule::class],
    dependencies = [AppComponent::class, MainView::class]
)
interface BusinessListComponent {
    fun inject(businessListFragment: BusinessListFragment)
}