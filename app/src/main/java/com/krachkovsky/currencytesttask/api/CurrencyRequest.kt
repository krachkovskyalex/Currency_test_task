package com.krachkovsky.currencytesttask.api

import com.krachkovsky.currencytesttask.models.Currency
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRequest {

    @GET("rates?periodicity=0")
    fun getCurrency(@Query("ondate") date: String): Call<Currency>


}