package com.example.countries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countries.di.DaggerApiComponent
import com.example.countries.model.CountriesService
import com.example.countries.model.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel : ViewModel() {

   // private val countriesService = CountriesService()
    //Seperated model from viewmodel using dagger
   @Inject
   lateinit var countriesService : CountriesService

   init {
       DaggerApiComponent.create().inject(this)
   }

    //It sense ListViewModel using RX java , when this viewmodel is closed
    //we need to clear that connection using this variable
    private val disposable = CompositeDisposable()


    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        //mock data for trial
        /*  val mockData = listOf(
              Country("CountryA"),
              Country("CountryB"),
              Country("CountryC"),
              Country("CountryD"),
              Country("CountryE"),
              Country("CountryF"),
              Country("CountryG"),
              Country("CountryH"),
          )
          countryLoadError.value = false
          loading.value = false
          countries.value = mockData*/

        loading.value = true
        disposable.add(
            countriesService.getCountries()
                //getCountries will be run on new thread out of main thread
                .subscribeOn(Schedulers.io())
                //But result of above will be observed on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(value: List<Country>?) {
                        countries.value = value
                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable?) {
                        countryLoadError.value = true
                        loading.value = false
                    }

                }
                ))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}