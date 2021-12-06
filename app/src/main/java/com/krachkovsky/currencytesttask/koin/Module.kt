package com.krachkovsky.currencytesttask.koin

import com.krachkovsky.currencytesttask.adapter.CurrencyAdapter
import com.krachkovsky.currencytesttask.api.CurrencyApi
import com.krachkovsky.currencytesttask.ui.CurrencyViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { CurrencyAdapter() }
    single { CurrencyApi() }
    viewModel { CurrencyViewModel(get()) }

}