package com.krachkovsky.currencytesttask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krachkovsky.currencytesttask.data.CurrencyInfo
import com.krachkovsky.currencytesttask.data.CurrencyStorage
import com.krachkovsky.currencytesttask.databinding.CurrencyItemBinding
import com.krachkovsky.currencytesttask.models.CurrencyData

class CurrencyAdapter(private val currencyStorage: CurrencyStorage) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val currencyData: MutableList<CurrencyListItem> = mutableListOf()

    fun setData(newCurrency: CurrencyData) {
        currencyData.clear()
        val savedItems = currencyStorage.getCurrencyInfos()
        newCurrency.current.forEachIndexed { index, current ->
            currencyData.add(
                CurrencyListItem(
                    abbreviation = current.abbreviation,
                    id = current.id,
                    name = current.name,
                    nextRate = newCurrency.next[index].officialRate,
                    currentRate = current.officialRate,
                    scale = current.scale,
                    orderPosition = if (savedItems.isEmpty()) {
                        index
                    } else {
                        //TODO optimize with hashMap
                        savedItems.find { t -> t.code == current.id }!!.sortOrder
                    }
                )
            )
        }
        if (currencyStorage.getCurrencyInfos().isEmpty()) {
            currencyStorage.saveCurrencyInfos(currencyData.map {
                CurrencyInfo(it.id, true, it.orderPosition)
            })
        }
        currencyData.sortBy { it.orderPosition }

        notifyDataSetChanged()
    }

    fun swap(oldPosition: Int, newPosition: Int) {
        val item = currencyData.removeAt(oldPosition)
        currencyData.add(newPosition, item)
        saveCurrency()
    }


    fun saveCurrency() {
        val currencyInfos = currencyData.mapIndexed { index, item ->
            CurrencyInfo(item.id, true, index)
        }
        currencyStorage.saveCurrencyInfos(currencyInfos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        with(holder) {
            with(currencyData[position]) {
                with(binding) {
                    tvCurAbbreviation.text = abbreviation
                    tvCurScale.text = scale.toString()
                    tvCurName.text = name.replaceFirstChar { it.lowercase() }
                    tvCurCurrent.text = currentRate.toString()
                    tvCurNext.text = nextRate.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return currencyData.size
    }
}
