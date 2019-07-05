package com.gp.yelp.screen.main

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.gp.yelp.network.model.Address
import com.gp.yelp.network.model.ApiResponse
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val placesClient: PlacesClient) :
    ViewModel(),
    LifecycleObserver {

    private val disposable = CompositeDisposable()

    val liveDataAddress = MutableLiveData<ApiResponse<List<Address>>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    fun search(term: String, northeast: LatLng, southwest: LatLng) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("ph")
            .setLocationRestriction(RectangularBounds.newInstance(northeast, southwest))
            .setQuery(term)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener {
                val addresses = mutableListOf<Address>()
                for (prediction in it!!.autocompletePredictions) {
                    addresses.add(
                        Address(
                            prediction.placeId,
                            prediction.getFullText(null).toString(),
                            prediction.getSecondaryText(null).toString()
                        )
                    )
                }

                liveDataAddress.postValue(ApiResponse(addresses))
            }
            .addOnFailureListener {
                liveDataAddress.postValue(ApiResponse(throwable = it))
            }
    }
}
