package com.krachkovsky.currencytesttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krachkovsky.currencytesttask.adapter.CurrencyAdapter
import com.krachkovsky.currencytesttask.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val currencyViewModel: CurrencyViewModel by viewModel()
    private val currencyAdapter: CurrencyAdapter by inject()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupRecyclerView()
        currencyViewModel.getCurrentCurrency()
        currencyViewModel.currencyData.observe(this, {
            currencyAdapter.setData(it)
            //TODO simple date format
            binding.tvCurrentDay.text = it.current[0].date.dropLast(9).split("-").reversed().joinToString(".")
            binding.tvNextDay.text = it.next[0].date.dropLast(9).split("-").reversed().joinToString(".")
        })
        itemTouchHelper.attachToRecyclerView(binding.rvCurrency)
    }


    private fun setupRecyclerView() {
        binding.rvCurrency.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private val itemTouchHelperCallback = object: ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            // Specify the directions of movement
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // Notify your adapter that an item is moved from x position to y position
            currencyAdapter.swap(viewHolder.adapterPosition, target.adapterPosition)
            currencyAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun isLongPressDragEnabled(): Boolean {
            // true: if you want to start dragging on long press
            // false: if you want to handle it yourself
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            // Handle action state changes
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            // Called by the ItemTouchHelper when the user interaction with an element is over and it also completed its animation
            // This is a good place to send update to your backend about changes
        }
    }

    val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

}