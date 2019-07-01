package com.gp.yelp.screen.filter

import com.gp.yelp.di.ActivityScope
import com.gp.yelp.di.AppComponent
import com.gp.yelp.di.FragmentScope
import com.gp.yelp.viewModel.FilterViewModelModule
import com.gp.yelp.viewModel.ViewModelFactoryModule
import com.gp.yelp.viewModel.MainViewModelModule
import dagger.Component

@FragmentScope
@Component(
    modules = [ViewModelFactoryModule::class, FilterViewModelModule::class],
    dependencies = [AppComponent::class]
)
interface FilterComponent {
    fun inject(dialogFragmentFilter: DialogFragmentFilter)
}