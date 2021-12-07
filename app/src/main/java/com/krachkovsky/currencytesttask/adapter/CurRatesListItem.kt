package com.krachkovsky.currencytesttask.adapter


data class CurRatesListItem(
    val abbreviation: String,
    val id: Int,
    val name: String,
    val nextRate: Double,
    val currentRate: Double,
    val scale: Int,
    var orderPosition: Int,
    var visible: Boolean = true
)