package com.carserviceapp.carserviceapp.Helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.carserviceapp.carserviceapp.Adapter.CarAdapter

//Unresolved reference: removeCarAt
//class ItemTouchHelper(private val adapter: ItemTouchHelper.Callback) : ItemTouchHelper.Callback() {
//    override fun getMovementFlags(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder
//    ): Int {
//        return 1
//    }
//
//    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//        return false
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int): Unit {
//        when (direction) {
//            ItemTouchHelper.LEFT -> {
//                adapter.removeCarAt(viewHolder.adapterPosition)
//            }
//        }
//    }
//}