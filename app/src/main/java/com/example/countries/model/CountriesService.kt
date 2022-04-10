package com.example.countries.model

import com.example.countries.di.DaggerApiComponent
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class CountriesService {
    //private val BASE_URL = "https://raw.githubusercontent.com"
    @Inject
    lateinit var api : CountriesApi

    init {
        //Removed from here coz. we need to have sepeeration b/n getCountries and
        //Retrofit for mocking purpose so used dagger 2
       /* api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CountriesApi::class.java)*/

        DaggerApiComponent.create().inject(this)
    }

    fun getCountries() : Single<List<Country>> {
        return api.getCountries()
    }
}