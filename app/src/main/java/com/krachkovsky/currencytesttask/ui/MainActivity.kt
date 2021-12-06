package com.krachkovsky.currencytesttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.krachkovsky.currencytesttask.adapter.CurrencyAdapter
import com.krachkovsky.currencytesttask.data.CurrencyStorage
import com.krachkovsky.currencytesttask.databinding.ActivityMainBinding
import com.krachkovsky.currencytesttask.models.Currency
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val currencyViewModel: CurrencyViewModel by viewModel()
    private val currencyAdapter: CurrencyAdapter by inject()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupRecyclerView()
        currencyViewModel.getCurrentCurrency()
        currencyViewModel.currencyData.observe(this, {
            currencyAdapter.setData(it)
            //TODO simple date format
            binding.tvCurrentDay.text = it.current[0].date.dropLast(9).split("-").reversed().joinToString(".")
            binding.tvNextDay.text = it.next[0].date.dropLast(9).split("-").reversed().joinToString(".")
        })
    }


    private fun setupRecyclerView() {
        binding.rvCurrency.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}