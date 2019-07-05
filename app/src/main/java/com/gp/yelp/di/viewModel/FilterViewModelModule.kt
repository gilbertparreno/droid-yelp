package com.gp.yelp.viewModel

import androidx.lifecycle.ViewModel
import com.gp.yelp.screen.filter.FilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FilterViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    abstract fun filterViewModel(filterViewModel: FilterViewModel): ViewModel
}