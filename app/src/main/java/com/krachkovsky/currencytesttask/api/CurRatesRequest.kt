package com.krachkovsky.currencytesttask.api

import com.krachkovsky.currencytesttask.models.CurRatesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurRatesRequest {

    @GET("rates?periodicity=0")
    fun getCurRates(@Query("ondate") date: String): Call<List<CurRatesItem>>


}