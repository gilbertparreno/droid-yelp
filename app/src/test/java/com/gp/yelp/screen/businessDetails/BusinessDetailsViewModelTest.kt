package com.gp.yelp.screen.businessList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.Business
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Reviews
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.network.repository.BusinessRepositoryInteractorImpl
import com.gp.yelp.network.service.BusinessService
import com.gp.yelp.screen.main.BusinessDetailsViewModel
import com.gp.yelp.screen.main.BusinessListViewModel
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.net.SocketException


@RunWith(JUnit4::class)
class BusinessDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var businessService: BusinessService

    lateinit var businessRepositoryInteractor: BusinessRepositoryInteractor

    lateinit var businessListViewModel: BusinessDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        businessRepositoryInteractor = BusinessRepositoryInteractorImpl(businessService)
        this.businessListViewModel = BusinessDetailsViewModel(businessRepositoryInteractor)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun getBusinessDetailsSuccess() {
        val business = mock<Business>()
        val reviews = mock<Reviews>()
        Mockito.`when`(this.businessService.getBusinessById(ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.just(business)
                }
        Mockito.`when`(this.businessService.getReviews(ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.just(reviews)
                }

        val observer = mock(Observer::class.java) as Observer<ApiResponse<Pair<Business, Reviews>>>
        this.businessListViewModel.liveDataBusinessDetails().observeForever(observer)

        this.businessListViewModel.getBusinessDetails(ArgumentMatchers.anyString())

        assertNotNull(this.businessListViewModel.liveDataBusinessDetails().value)
        assertNotNull(this.businessListViewModel.liveDataBusinessDetails().value?.data)
        assertNull(this.businessListViewModel.liveDataBusinessDetails().value?.throwable)
    }

    @Test
    fun getBusinessDetailsError() {
        Mockito.`when`(this.businessService.getBusinessById(ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.error<SocketException>(SocketException("Test error!"))
                }
        Mockito.`when`(this.businessService.getReviews(ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.error<SocketException>(SocketException("Test error!"))
                }

        val observer = mock(Observer::class.java) as Observer<ApiResponse<Pair<Business, Reviews>>>
        this.businessListViewModel.liveDataBusinessDetails().observeForever(observer)

        this.businessListViewModel.getBusinessDetails(ArgumentMatchers.anyString())

        assertNotNull(this.businessListViewModel.liveDataBusinessDetails().value)
        assertNull(this.businessListViewModel.liveDataBusinessDetails().value?.data)
        assertNotNull(this.businessListViewModel.liveDataBusinessDetails().value?.throwable)
    }
}