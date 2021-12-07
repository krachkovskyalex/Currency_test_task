package com.krachkovsky.currencytesttask.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CurRatesStorage(context: Context) {

    private val gson = Gson()

    private val sharedPref = context.getSharedPreferences(
        PREFERENCE_KEY, Context.MODE_PRIVATE)

    companion object {
        const val PREFERENCE_KEY = "prefs"
        const val SORTING_PREF = "sorting"
    }

    fun getCurrencyInfos(): List<CurRatesInfo> {
        val value = sharedPref.getString(SORTING_PREF, "[]")
//        val value = "[]"
        val itemType = object : TypeToken<List<CurRatesInfo>>() {}.type
        val itemList = gson.fromJson<List<CurRatesInfo>>(value, itemType)
        return itemList
    }
    fun saveCurrencyInfos(infos: List<CurRatesInfo>) {
        with (sharedPref.edit()) {
            val value = gson.toJson(infos)
            putString(SORTING_PREF, value)
            apply()
        }
    }
}