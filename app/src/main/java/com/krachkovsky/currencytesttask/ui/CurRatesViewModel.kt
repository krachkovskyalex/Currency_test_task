package com.krachkovsky.currencytesttask.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krachkovsky.currencytesttask.api.CurRatesApi
import com.krachkovsky.currencytesttask.models.CurRatesData
import com.krachkovsky.currencytesttask.models.CurRatesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*

class CurRatesViewModel(private val request: CurRatesApi) : ViewModel() {

    val responseError = MutableLiveData<Boolean>()
    val viewState = MutableLiveData<ViewState>()
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    var curRatesData: CurRatesData? = null

    init {
        viewState.value = ViewState.Loading
    }

    fun getCurrentCurrency() {
        viewState.value = ViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val curRatesCurrent: List<CurRatesItem>
            var currencyNext = getCurrency(getDate(1))
            if (currencyNext.isNullOrEmpty()) {
                curRatesCurrent = getCurrency(getDate(-1))
                currencyNext = getCurrency(getDate())
            } else {
                curRatesCurrent = getCurrency(getDate())
            }
            curRatesData = CurRatesData(curRatesCurrent, currencyNext)
            if (curRatesCurrent.isNullOrEmpty()) {
                viewState.postValue(ViewState.Error)
            } else {
                viewState.postValue(ViewState.CurRatesScreen(curRatesData!!))
            }

        }
    }

    suspend fun getCurrency(date: String): List<CurRatesItem> {
        try {
            val response = request.createApiRequest().getCurRates(date).awaitResponse()
            if (response.isSuccessful) {
                Log.e("getCurrentCurrency", response.body().toString())
                responseError.postValue(false)
                response.body()?.let { return it }
            }
        } catch (e: Exception) {
            responseError.postValue(true)
            Log.e("getCurrentCurrency", "Response Exception", e)
        }
        return listOf()
    }

    fun getDate(incrementDays: Int = 0): String {
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.DATE, incrementDays)
        return sdf.format(c.time)
    }

    fun startEdit() {
        viewState.value = ViewState.Editing
    }

    fun finishEdit() {
        viewState.value = ViewState.CurRatesScreen(curRatesData!!)
    }

    sealed class ViewState {
        object Loading : ViewState()
        object Editing : ViewState()
        object Error : ViewState()
        data class CurRatesScreen(val data: CurRatesData) : ViewState()
    }
}