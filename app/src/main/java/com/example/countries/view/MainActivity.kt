package com.example.countries.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.model.Country
import com.example.countries.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

//MainActivity acts as View in MVVM
class MainActivity : AppCompatActivity() {
    //lateinit ensures developer will add the data to variable before using
    //else it will through crash , if we didnt use it will force to initialise here only.
    lateinit var viewModel : ListViewModel

    private val countriesAdapter = CountryListAdapter(arrayListOf())

    lateinit var mRecyclerview : RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerview = findViewById<RecyclerView>(R.id.contriesList)

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        mRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
             swipeRefreshLayout.isRefreshing = false
             viewModel.refresh()
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        //Here it means countries
        viewModel.countries.observe(this, Observer { countries: List<Country> ->
            countries.let {
                mRecyclerview.visibility = View.VISIBLE
                countriesAdapter.updateCountries(it) }
        })

        viewModel.countryLoadError.observe(this, Observer { isError ->
            isError?.let {list_error.visibility = if (it) View.VISIBLE else View.GONE}
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {  loading_view.visibility = if(it) View.VISIBLE else View.GONE
            if (it){
                list_error.visibility = View.GONE
                mRecyclerview.visibility = View.GONE
            }
            }

        })
    }
}