package com.gp.yelp.viewModel

import androidx.lifecycle.ViewModel
import com.gp.yelp.screen.main.BusinessListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BusinessListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BusinessListViewModel::class)
    abstract fun businessListViewModel(businessListViewModel: BusinessListViewModel): ViewModel
}