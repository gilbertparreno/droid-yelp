package com.gp.yelp.screen.main

import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gp.yelp.network.model.Address
import com.gp.yelp.network.model.ApiResponse
import com.gp.yelp.network.model.BusinessList
import com.gp.yelp.network.model.Category
import com.gp.yelp.network.repository.BusinessRepositoryInteractor
import com.gp.yelp.utils.DEFAULT_RADIUS
import com.gp.yelp.utils.DEFAULT_SORT_BY_ALIAS
import com.gp.yelp.utils.SharedPreferenceUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.operators.single.SingleCreate
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class BusinessListViewModel @Inject constructor(
        private val businessRepositoryInteractor: BusinessRepositoryInteractor,
        private val sharedPreferenceUtil: SharedPreferenceUtil,
        private val placesClient: PlacesClient
) :
        ViewModel(),
        LifecycleObserver {

    val liveDataProjects = MutableLiveData<ApiResponse<BusinessList>>()
    val liveDataPlaceIdtoLatLng = MutableLiveData<ApiResponse<LatLng>>()

    private val disposable = CompositeDisposable()

    private var radius: Int = DEFAULT_RADIUS
    private var openNow = false
    private lateinit var sortBy: String
    private lateinit var categories: String

    var businessName: String? = null
    var address: Address? = null
    var customLatLng: LatLng? = null
    private lateinit var deviceCurrentLocation: LatLng

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        refreshFilters()
    }

    fun onLocationAvailable(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        deviceCurrentLocation = latLng
        getProjectList()
    }

    private fun refreshFilters() {
        radius = sharedPreferenceUtil.getInt(SharedPreferenceUtil.Key.RADIUS, DEFAULT_RADIUS)
        openNow = sharedPreferenceUtil.getBoolean(SharedPreferenceUtil.Key.OPEN_NOW, false)
        sortBy = sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.SORT_BY, DEFAULT_SORT_BY_ALIAS)
        val cat = sharedPreferenceUtil.getString(SharedPreferenceUtil.Key.CATEGORIES, "[]")
        val listType = object : TypeToken<ArrayList<Category>>() {}.type
        categories = Gson().fromJson<List<Category>>(cat, listType)
                .toTypedArray()
                .joinToString(separator = ",") {
                    it.alias
                }
        Timber.d(categories)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.clear()
    }

    fun getProjectList() {
        refreshFilters()
        val latLng = if (customLatLng != null) customLatLng else deviceCurrentLocation
        disposable.add(businessRepositoryInteractor.searchBusiness(businessName, latLng!!.latitude, latLng.longitude, radius, sortBy, openNow, categories)
                .subscribe(
                        { data ->
                            liveDataProjects.postValue(ApiResponse(data))
                        }, { throwable ->
                    liveDataProjects.postValue(ApiResponse(throwable = throwable))
                }
                ))
    }

    fun searchByLatlngAndName(name: String, address: Address?) {
        this.businessName = name
        this.address = address

        if (address != null) {
            disposable.add(SingleCreate<LatLng> { emitter ->
                val request = FetchPlaceRequest.builder(address.placeId, Arrays.asList(Place.Field.LAT_LNG)).build()
                placesClient.fetchPlace(request).addOnSuccessListener {
                    emitter.onSuccess(it.place.latLng!!)
                }.addOnFailureListener {
                    Timber.e(it)
                    emitter.onError(it)
                }
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        customLatLng = it
                        getProjectList()
                    }, {
                        liveDataPlaceIdtoLatLng.postValue(ApiResponse(throwable = it))
                    }))
        } else {
            getProjectList()
        }
    }
}
