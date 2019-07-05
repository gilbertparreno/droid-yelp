package com.gp.yelp.viewModel

import androidx.lifecycle.ViewModel
import com.gp.yelp.screen.main.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun searchViewModel(searchViewModel: SearchViewModel): ViewModel
}