package com.krachkovsky.currencytesttask.adapter


data class CurrencyListItem(
    val abbreviation: String,
    val id: Int,
    val name: String,
    val nextRate: Double,
    val currentRate: Double,
    val scale: Int,
    var orderPosition: Int
)