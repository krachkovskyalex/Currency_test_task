package com.krachkovsky.currencytesttask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krachkovsky.currencytesttask.databinding.CurrencyItemBinding
import com.krachkovsky.currencytesttask.models.Currency
import com.krachkovsky.currencytesttask.models.CurrencyItem

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val currentCurrencyList: MutableList<CurrencyItem> = ArrayList()
    private val previousCurrencyList: MutableList<CurrencyItem> = ArrayList()

    fun setCurrentData(newCurrentCurrency: Currency, newPreviousCurrency: Currency) {
        currentCurrencyList.clear()
        currentCurrencyList.addAll(newCurrentCurrency)
        previousCurrencyList.clear()
        previousCurrencyList.addAll(newPreviousCurrency)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        with(holder) {
            with(currentCurrencyList[position]) {
                with(binding) {
                    tvCurAbbreviation.text = abbreviation
                    tvCurScale.text = scale.toString()
                    tvCurName.text = name.replaceFirstChar { it.lowercase() }
                    tvCurCurrent.text = officialRate.toString()
                }
            }
            with(previousCurrencyList[position]) {
                with(binding) {
                    tvCurPrevious.text = officialRate.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return currentCurrencyList.size
    }
}