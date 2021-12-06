package com.krachkovsky.currencytesttask.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krachkovsky.currencytesttask.api.CurrencyApi
import com.krachkovsky.currencytesttask.models.Currency
import com.krachkovsky.currencytesttask.models.CurrencyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*

class CurrencyViewModel(private val request: CurrencyApi) : ViewModel() {

    val currencyData = MutableLiveData<CurrencyData>()
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    fun getCurrentCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            val currencyToday = getCurrency(getDate(-1))
            val currencyTomorrow = getCurrency(getDate())
            //TODO handle null
            currencyData.postValue(CurrencyData(currencyToday!!, currencyTomorrow!!))
        }
    }

    suspend fun getCurrency(date: String): Currency? {
        try {
            val response = request.createApiRequest().getCurrency(date).awaitResponse()
            if (response.isSuccessful) {
                Log.e("getCurrentCurrency", response.body().toString())
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("getCurrentCurrency", "Response Exception", e)
        }
        return null
    }

    fun getDate(incrementDays: Int = 0): String {
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DATE, incrementDays)
        return sdf.format(c.time)
    }
}