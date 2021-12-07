package com.krachkovsky.currencytesttask.koin

import com.krachkovsky.currencytesttask.adapter.CurRatesAdapter
import com.krachkovsky.currencytesttask.api.CurRatesApi
import com.krachkovsky.currencytesttask.data.CurRatesStorage
import com.krachkovsky.currencytesttask.ui.CurRatesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { CurRatesStorage(androidContext()) }
    single { CurRatesAdapter(get()) }
    single { CurRatesApi() }
    viewModel { CurRatesViewModel(get()) }

}