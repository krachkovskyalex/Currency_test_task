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

    val currencyData = MutableLiveData<Currency>()

    fun getCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = request.createApiRequest().getCurrency().awaitResponse()
                if (response.isSuccessful) {
                    currencyData.postValue(response.body())
                    Log.e("AAA", response.body().toString())
                }
            } catch (e: Exception) {
                Log.e("AAA", "Response Exception", e)
            }
        }
    }
}