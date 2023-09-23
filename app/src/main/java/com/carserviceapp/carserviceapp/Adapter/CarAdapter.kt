package com.carserviceapp.carserviceapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carserviceapp.carserviceapp.Models.Car
import com.carserviceapp.carserviceapp.R

class CarAdapter(private val cars: MutableList<Car>) :
    RecyclerView.Adapter<CarAdapter.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(car: Car)
    }

    var onItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carBrand: TextView = itemView.findViewById(R.id.displaycarbrand)
        val carType: TextView = itemView.findViewById(R.id.displaycartype)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(cars[position])
                }
            }
        }
    }
//    fun removeCarAt(position: Int) {
//        cars.removeAt(position)
//        notifyItemRemoved(position)
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dialog_cardetail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = cars[position]
        holder.carBrand.text = car.Car_Brand
        holder.carType.text = car.Car_Type
    }

    override fun getItemCount(): Int {
        return cars.size
    }

//    init {
//        itemTouchHelper.attachToRecyclerView(null)
//    }

//    override fun getMovementFlags(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder
//    ): Int {
//        return ItemTouchHelper.SimpleCallback.makeMovementFlags(0, ItemTouchHelper.RIGHT)
//    }
//
//    override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//    ): Boolean {
//        return false
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        val position = viewHolder.adapterPosition
//        val deletedCar = cars[position]
//
//        // Remove the swiped car from the list
//        cars.removeAt(position)
//        notifyItemRemoved(position)
//    }
}

