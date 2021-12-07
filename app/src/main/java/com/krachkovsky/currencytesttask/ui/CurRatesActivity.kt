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
                    return@setOnMenuItemClickListener true
                }
                R.id.done -> {
                    curRatesViewModel.finishEdit()
                    item.isVisible = false
                    binding.myToolbar.menu.findItem(R.id.settings).isVisible = true
                    return@setOnMenuItemClickListener true
                }
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
                binding.tvLoadingError.isVisible = false
                binding.myToolbar.menu.findItem(R.id.settings).isVisible = false
            }
            is CurRatesViewModel.ViewState.Editing -> {
                itemTouchHelperCallback.setDragEnable(true)
                curRatesAdapter.setEditing(true)
                binding.pbLoading.isVisible = false
                binding.tvLoading.isVisible = false
                binding.rvCurrency.isVisible = true
                binding.tvLoadingError.isVisible = false
            }
            is CurRatesViewModel.ViewState.CurRatesScreen -> {
                itemTouchHelperCallback.setDragEnable(false)
                curRatesAdapter.setEditing(false)
                curRatesAdapter.setData(viewState.data)
                //TODO simple date format
                binding.tvCurrentDay.text =
                    viewState.data.current[0].date.dropLast(9).split("-").reversed()
                        .joinToString(".")
                binding.tvNextDay.text =
                    viewState.data.next[0].date.dropLast(9).split("-").reversed().joinToString(".")
                binding.pbLoading.isVisible = false
                binding.tvLoading.isVisible = false
                binding.rvCurrency.isVisible = true
                binding.tvLoadingError.isVisible = false
                binding.myToolbar.menu.findItem(R.id.settings).isVisible = true
            }
            is CurRatesViewModel.ViewState.Error -> {
                binding.pbLoading.isVisible = false
                binding.tvLoading.isVisible = false
                binding.rvCurrency.isVisible = false
                binding.tvLoadingError.isVisible = true
                binding.myToolbar.menu.findItem(R.id.settings).isVisible = false

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
        private var canDrag = false

        fun setDragEnable(canDrag: Boolean) {
            this.canDrag = canDrag
        }


        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = if (canDrag) {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            } else {
                0
            }
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            curRatesAdapter.swapCurRatesItemPosition(
                viewHolder.adapterPosition,
                target.adapterPosition
            )
            curRatesAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
        }
    }

    private val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

}