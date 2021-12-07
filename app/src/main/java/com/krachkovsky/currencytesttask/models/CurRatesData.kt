package com.krachkovsky.currencytesttask.models

data class CurRatesData(
    val current: List<CurRatesItem>,
    val next: List<CurRatesItem>
)
