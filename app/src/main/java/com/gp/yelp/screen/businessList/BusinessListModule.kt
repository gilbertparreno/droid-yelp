package com.gp.yelp.screen.main

import com.gp.yelp.di.FragmentScope
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.network.repository.BusinessRepositoryInteractorImpl
import com.gp.yelp.network.service.BusinessService
import com.gp.yelp.screen.businessList.BusinessListView
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class BusinessListModule(val businessListFragment: BusinessListFragment) {

    @Provides
    @FragmentScope
    fun providesBusinessRepositoryInteractor(businessService: BusinessService): BusinessRepositoryInteractor {
        return BusinessRepositoryInteractorImpl(businessService)
    }

    @Provides
    @FragmentScope
    fun providesBusinessService(retrofit: Retrofit): BusinessService {
        return retrofit.create(BusinessService::class.java)
    }

    @Provides
    @FragmentScope
    fun providesBusinessListView(): BusinessListView {
        return businessListFragment
    }
}