package com.krachkovsky.currencytesttask.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krachkovsky.currencytesttask.api.CurrencyApi
import com.krachkovsky.currencytesttask.models.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class CurrencyViewModel(val request: CurrencyApi) : ViewModel() {

    val currentCurrencyData = MutableLiveData<Currency>()
    val previousCurrencyData = MutableLiveData<Currency>()

    fun getCurrentCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = request.createApiRequest().getCurrentCurrency().awaitResponse()
                if (response.isSuccessful) {
                    currentCurrencyData.postValue(response.body())
                    Log.e("AAA", response.body().toString())
                }
            } catch (e: Exception) {
                Log.e("AAA", "Response Exception", e)
            }
        }
    }
    fun getPreviousCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = request.createApiRequest().getPreviousCurrency().awaitResponse()
                if (response.isSuccessful) {
                    previousCurrencyData.postValue(response.body())
                    Log.e("AAA", response.body().toString())
                }
            } catch (e: Exception) {
                Log.e("AAA", "Response Exception", e)
            }
        }
    }
}