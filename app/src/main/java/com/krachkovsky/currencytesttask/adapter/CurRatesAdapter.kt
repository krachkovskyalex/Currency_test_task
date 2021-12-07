package com.krachkovsky.currencytesttask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.krachkovsky.currencytesttask.data.CurRatesInfo
import com.krachkovsky.currencytesttask.data.CurRatesStorage
import com.krachkovsky.currencytesttask.databinding.CurrencyItemBinding
import com.krachkovsky.currencytesttask.models.CurRatesData

class CurRatesAdapter(private val curRatesStorage: CurRatesStorage) :
    RecyclerView.Adapter<CurRatesAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val curRatesData: MutableList<CurRatesListItem> = mutableListOf()
    var isEditMode = false

    fun setData(newCurRates: CurRatesData) {
        curRatesData.clear()
        val savedItems = curRatesStorage.getCurrencyInfos()
        newCurRates.current.forEachIndexed { index, current ->
            curRatesData.add(
                CurRatesListItem(
                    abbreviation = current.abbreviation,
                    id = current.id,
                    name = current.name,
                    nextRate = newCurRates.next[index].officialRate,
                    currentRate = current.officialRate,
                    scale = current.scale,
                    orderPosition = if (savedItems.isEmpty()) {
                        index
                    } else {
                        //TODO optimize with hashMap
                        savedItems.find { t -> t.code == current.id }!!.sortOrder
                    },
                    visible = if (savedItems.isEmpty()) {
                        true
                    } else {
                        //TODO optimize with hashMap
                        savedItems.find { t -> t.code == current.id }!!.isVisible
                    }
                )
            )
        }
        if (curRatesStorage.getCurrencyInfos().isEmpty()) {
            curRatesStorage.saveCurrencyInfos(curRatesData.map {
                CurRatesInfo(it.id, it.visible, it.orderPosition)
            })
        }
        curRatesData.sortBy { it.orderPosition }

        notifyDataSetChanged()
    }

    fun swap(oldPosition: Int, newPosition: Int) {
        val item = curRatesData.removeAt(oldPosition)
        curRatesData.add(newPosition, item)
        saveCurrency()
    }

    fun setEditing(value: Boolean) {
        isEditMode = value
        if (isEditMode.not()) {
            saveCurrency()
        }

        notifyDataSetChanged()
    }

    private fun saveCurrency() {
        val currencyInfos = curRatesData.mapIndexed { index, item ->
            CurRatesInfo(item.id, item.visible, index)
        }
        curRatesStorage.saveCurrencyInfos(currencyInfos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        with(holder) {
            with(curRatesData[position]) {
                with(binding) {
                    tvCurAbbreviation.text = abbreviation
                    tvCurScale.text = scale.toString()
                    tvCurName.text = name.replaceFirstChar { it.lowercase() }
                    tvCurCurrent.text = currentRate.toString()
                    tvCurNext.text = nextRate.toString()
                    llCurValue.isVisible = isEditMode.not()
                    llCurSwitch.isVisible = isEditMode
                    swCurVisible.isChecked = visible
                    swCurVisible.setOnCheckedChangeListener { buttonView, isChecked ->
                        visible = isChecked
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return curRatesData.size
    }
}
