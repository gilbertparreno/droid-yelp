package com.gp.yelp.screen.businessList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.network.repository.BusinessRepositoryInteractorImpl
import com.gp.yelp.network.service.BusinessService
import com.gp.yelp.screen.main.BusinessListViewModel
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
class BusinessListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var businessService: BusinessService

    @Mock
    lateinit var businessListView: BusinessListView

    lateinit var businessRepositoryInteractor: BusinessRepositoryInteractor

    lateinit var businessListViewModel: BusinessListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        businessRepositoryInteractor = BusinessRepositoryInteractorImpl(businessService)
        this.businessListViewModel = BusinessListViewModel(businessRepositoryInteractor, businessListView)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun searchBusinessByAddressSuccess() {
        val settings = Settings(address = "Some location")
        val businessList = BusinessList(0, BusinessList.Region(BusinessList.Center(0.0, 0.0)), listOf())
        Mockito.`when`(this.businessService.searchBusinessByByAddress(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.just(businessList)
                }

        val observer = mock(Observer::class.java) as Observer<ApiResponse<BusinessList>>
        this.businessListViewModel.getBusinessListLiveData().observeForever(observer)

        this.businessListViewModel.searchBusiness(settings)

        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value)
        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value?.data)
        assertNull(this.businessListViewModel.getBusinessListLiveData().value?.throwable)
    }

    @Test
    fun searchBusinessByAddressError() {
        val settings = Settings(address = "Some location")
        Mockito.`when`(this.businessService.searchBusinessByByAddress(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.error<SocketException>(SocketException("Test error!"))
                }

        val observer = mock(Observer::class.java) as Observer<ApiResponse<BusinessList>>
        this.businessListViewModel.getBusinessListLiveData().observeForever(observer)

        this.businessListViewModel.searchBusiness(settings)

        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value)
        assertNull(this.businessListViewModel.getBusinessListLiveData().value?.data)
        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value?.throwable)
    }

    @Test
    fun searchBusinessLatLngSuccess() {
        val settings = Settings(latitude = 0.0, longitude = 0.0)
        val businessList = BusinessList(0, BusinessList.Region(BusinessList.Center(0.0, 0.0)), listOf())
        Mockito.`when`(this.businessService.searchBusinessByLatlng(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean(),
                ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.just(businessList)
                }

        val observer = mock(Observer::class.java) as Observer<ApiResponse<BusinessList>>
        this.businessListViewModel.getBusinessListLiveData().observeForever(observer)

        this.businessListViewModel.searchBusiness(settings)

        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value)
        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value?.data)
        assertNull(this.businessListViewModel.getBusinessListLiveData().value?.throwable)
    }

    @Test
    fun searchBusinessLatLngError() {
        val settings = Settings(latitude = 0.0, longitude = 0.0)
        Mockito.`when`(this.businessService.searchBusinessByLatlng(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyDouble(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean(),
                ArgumentMatchers.anyString()))
                .thenAnswer {
                    return@thenAnswer Single.error<SocketException>(SocketException("Test error!"))
                }

        val observer = mock(Observer::class.java) as Observer<ApiResponse<BusinessList>>
        this.businessListViewModel.getBusinessListLiveData().observeForever(observer)

        this.businessListViewModel.searchBusiness(settings)

        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value)
        assertNull(this.businessListViewModel.getBusinessListLiveData().value?.data)
        assertNotNull(this.businessListViewModel.getBusinessListLiveData().value?.throwable)
    }
}