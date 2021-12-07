package com.krachkovsky.currencytesttask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krachkovsky.currencytesttask.R
import com.krachkovsky.currencytesttask.adapter.CurRatesAdapter
import com.krachkovsky.currencytesttask.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class CurRatesActivity : AppCompatActivity() {

    private val curRatesViewModel: CurRatesViewModel by viewModel()
    private val curRatesAdapter: CurRatesAdapter by inject()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.myToolbar.title = "My title"
        setupRecyclerView()
        curRatesViewModel.getCurrentCurrency()
        curRatesViewModel.viewState.observe(this, {
            renderViewState(it)
        })
        itemTouchHelper.attachToRecyclerView(binding.rvCurrency)
        curRatesViewModel.responseError.observe(this, {
            if (it) {
                binding.tvLoadingError.visibility = View.VISIBLE
            } else {
                binding.tvLoadingError.visibility = View.GONE
            }
        })
        binding.myToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    curRatesViewModel.startEdit()
                    item.isVisible = false
                    binding.myToolbar.menu.findItem(R.id.done).isVisible = true
                    return@setOnMenuItemClickListener true}
                R.id.done -> {
                    curRatesViewModel.finishEdit()
                    item.isVisible = false
                    binding.myToolbar.menu.findItem(R.id.settings).isVisible = true
                    return@setOnMenuItemClickListener true}
            }
            false
        }
    }

    private fun renderViewState(viewState: CurRatesViewModel.ViewState) {
        when (viewState) {
            is CurRatesViewModel.ViewState.Loading -> {
                binding.pbLoading.isVisible = true
                binding.tvLoading.isVisible = true
                binding.rvCurrency.isVisible = false
            }
            is CurRatesViewModel.ViewState.Editing -> {
                curRatesAdapter.setEditing(true)
                binding.pbLoading.isVisible = false
                binding.tvLoading.isVisible = false
                binding.rvCurrency.isVisible = true
            }
            is CurRatesViewModel.ViewState.CurRatesScreen -> {
                curRatesAdapter.setEditing(false)
                curRatesAdapter.setData(viewState.data)
                //TODO simple date format
                binding.tvCurrentDay.text =
                    viewState.data.current[0].date.dropLast(9).split("-").reversed().joinToString(".")
                binding.tvNextDay.text =
                    viewState.data.next[0].date.dropLast(9).split("-").reversed().joinToString(".")
                binding.pbLoading.isVisible = false
                binding.tvLoading.isVisible = false
                binding.rvCurrency.isVisible = true
            }
        }
    }


    private fun setupRecyclerView() {
        binding.rvCurrency.apply {
            adapter = curRatesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
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
            curRatesAdapter.swap(viewHolder.adapterPosition, target.adapterPosition)
            curRatesAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
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