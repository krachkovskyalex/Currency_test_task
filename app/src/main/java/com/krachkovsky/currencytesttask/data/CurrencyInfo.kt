package com.krachkovsky.currencytesttask.data

import com.google.gson.annotations.SerializedName

data class CurrencyInfo(
    @SerializedName("code")
    val code: Int,
    @SerializedName("isVisible")
    val isVisible: Boolean,
    @SerializedName("sortOrder")
    val sortOrder: Int
)