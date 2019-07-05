package com.gp.yelp.screen.main

import com.gp.yelp.di.ActivityScope
import com.gp.yelp.di.AppComponent
import com.gp.yelp.di.FragmentScope
import com.gp.yelp.viewModel.ViewModelFactoryModule
import com.gp.yelp.viewModel.SearchViewModelModule
import dagger.Component

@FragmentScope
@Component(
    modules = [SearchModule::class, ViewModelFactoryModule::class, SearchViewModelModule::class],
    dependencies = [AppComponent::class, MainView::class]
)
interface SearchComponent {
    fun inject(searchFragment: SearchFragment)
}