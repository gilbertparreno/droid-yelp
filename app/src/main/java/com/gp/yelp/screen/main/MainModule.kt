package com.gp.yelp.screen.main

import com.gp.yelp.di.ActivityScope
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.network.repository.BusinessRepositoryInteractorImpl
import com.gp.yelp.network.service.BusinessService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun providesBusinessRepositoryInteractor(businessService: BusinessService): BusinessRepositoryInteractor {
        return BusinessRepositoryInteractorImpl(businessService)
    }

    @Provides
    @ActivityScope
    fun providesBusinessService(retrofit: Retrofit): BusinessService {
        return retrofit.create(BusinessService::class.java)
    }
}