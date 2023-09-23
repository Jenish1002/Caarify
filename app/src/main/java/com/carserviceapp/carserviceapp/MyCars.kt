package com.carserviceapp.carserviceapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carserviceapp.carserviceapp.Adapter.CarAdapter
import com.carserviceapp.carserviceapp.Models.Car
import com.carserviceapp.carserviceapp.databinding.FragmentMycarsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// HELP ME REMOVE ITEM OF CAR LIST USING ITEMTOUCHHEKLPER BELOW CODE SNIPPET
class MyCars : Fragment() {

    private lateinit var mycarsBinding: FragmentMycarsBinding
    private lateinit var carAdapter: CarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mycarsBinding = FragmentMycarsBinding.inflate(inflater, container, false)

        // add cars intent
        mycarsBinding.btnAddCars.setOnClickListener {
            val intent = Intent(requireContext(), AddCar::class.java)
            startActivity(intent)
        }

        //display  car cards
        displayCars()
        return mycarsBinding.root
    }

    private fun displayCars() {
        val user = FirebaseAuth.getInstance().currentUser
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Customer").child(user!!.uid).child("Cars")
        val carList = mutableListOf<Car>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (carSnapshot in snapshot.children) {
                    val carBrand = carSnapshot.child("Car_Brand").value as? String ?: ""
                    val carType = carSnapshot.child("Car_Type").value as? String ?: ""
                    val fuelType = carSnapshot.child("Car_Fuel").value as? String ?: ""
                    val carName = carSnapshot.child("Car_Name").value as? String ?: ""
                    val car = Car(carName, carType, fuelType, carBrand)
                    carList.add(car)
                }
                carAdapter = CarAdapter(carList)

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val deletedCar: Car = carList[viewHolder.adapterPosition]
                        val position = viewHolder.adapterPosition
                        carList.removeAt(position)
                        carAdapter.notifyItemRemoved(position)
                        Snackbar.make(
                            mycarsBinding.rvLayout,
                            "Deleted ${deletedCar.Car_Type}",
                            Snackbar.LENGTH_LONG
                        ).setAction("Undo") {
                            carList.add(position, deletedCar)
                            carAdapter.notifyItemInserted(position)
                        }.show()
                    }


                }).attachToRecyclerView(mycarsBinding.rvLayout)

                mycarsBinding.rvLayout.layoutManager = LinearLayoutManager(requireContext())
                mycarsBinding.rvLayout.adapter = carAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyCars()
    }
}


// Attach a listener to get the car details for the current user
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
// Clear the card view
//                mycarsBinding.carsContainer.removeAllViews()
//
//                // Inflate the layout for the card view and populate it with the car details
//                for (carSnapshot in snapshot.children) {
//                    val carData = carSnapshot.getValue(Car::class.java)
//                    val carView = layoutInflater.inflate(R.layout.car_card, null)
//
//                    carView.findViewById<TextView>(R.id.car_name).text = carData?.carName
//                    carView.findViewById<TextView>(R.id.car_brand).text = carData?.carBrand
//                    carView.findViewById<TextView>(R.id.car_model).text = carData?.carModel
//                    carView.findViewById<TextView>(R.id.car_number).text = carData?.carNumber
//
//                    mycarsBinding.carsContainer.addView(carView)
//