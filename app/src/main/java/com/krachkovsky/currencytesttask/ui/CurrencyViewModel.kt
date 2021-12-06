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

class CurrencyViewModel(val request: CurrencyApi) : ViewModel() {

    val currencyData = MutableLiveData<CurrencyData>()
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    fun getCurrentCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            val currencyToday = getCurrency(getDate())
            val currencyTomorrow = getCurrency(getDate(1))
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
//    fun getPreviousCurrency() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = request.createApiRequest().getPreviousCurrency().awaitResponse()
//                if (response.isSuccessful) {
//                    previousCurrencyData.postValue(response.body())
//                    Log.e("AAA", response.body().toString())
//                }
//            } catch (e: Exception) {
//                Log.e("AAA", "Response Exception", e)
//            }
//        }
//    }
}