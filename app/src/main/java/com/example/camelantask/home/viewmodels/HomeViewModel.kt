package com.example.camelantask.home.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.camelantask.common.CommonState
import com.example.camelantask.entities.images.ImageResponse
import com.example.camelantask.entities.places.PlacesResponse
import com.example.camelantask.repository.PlacesRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver

class HomeViewModel(private val placesRepository: PlacesRepository) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val states = MutableLiveData<CommonState<PlacesResponse>>()
    private val imageState = MutableLiveData<CommonState<ImageResponse>>()

    fun getPlaces(latLong: String, limit: Int) {
        disposable.add(
            placesRepository.getPlaces(latLong, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { states.value = CommonState.ShowLoading }
                .doFinally { states.value = CommonState.LoadingFinished }
                .subscribeWith(object : DisposableSingleObserver<PlacesResponse>() {
                    override fun onSuccess(t: PlacesResponse) {
                        states.value = CommonState.Success(t)
                    }

                    override fun onError(e: Throwable) {
                        states.value = CommonState.Error(e)
                    }

                })
        )
    }

    fun getImage(id: String) {
        // todo make a zip request to call multiple requests using retrofit
    }

    fun observeVeStates(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<CommonState<PlacesResponse>>
    ) {
        states.observe(lifecycleOwner, observer)
    }

    fun observeImageState(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<CommonState<ImageResponse>>
    ) {
        imageState.observe(lifecycleOwner, observer)
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed)
            disposable.dispose()
    }
}