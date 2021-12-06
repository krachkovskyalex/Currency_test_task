package com.krachkovsky.currencytesttask.api

import com.krachkovsky.currencytesttask.models.Currency
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyRequest {

    @GET("rates?periodicity=0")
    fun getCurrency(): Call<Currency>

}