package com.gp.yelp.viewModel

import androidx.lifecycle.ViewModel
import com.gp.yelp.screen.main.BusinessDetailsViewModel
import com.gp.yelp.screen.main.BusinessListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BusinessDetailsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BusinessDetailsViewModel::class)
    abstract fun businessDetailsViewModel(businessDetailsViewModel: BusinessDetailsViewModel): ViewModel
}