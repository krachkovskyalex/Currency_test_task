package com.krachkovsky.currencytesttask.koin

import com.krachkovsky.currencytesttask.adapter.CurrencyAdapter
import com.krachkovsky.currencytesttask.api.CurrencyApi
import com.krachkovsky.currencytesttask.data.CurrencyStorage
import com.krachkovsky.currencytesttask.ui.CurrencyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { CurrencyStorage(androidContext()) }
    single { CurrencyAdapter(get()) }
    single { CurrencyApi() }
    viewModel { CurrencyViewModel(get()) }

}