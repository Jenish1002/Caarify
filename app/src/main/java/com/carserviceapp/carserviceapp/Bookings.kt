//
package com.carserviceapp.carserviceapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carserviceapp.carserviceapp.Adapter.CarAdapter
import com.carserviceapp.carserviceapp.Models.Car
import com.carserviceapp.carserviceapp.Models.Requests
import com.carserviceapp.carserviceapp.databinding.FragmentBookingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.*

//
class Bookings : Fragment() {

    //    private lateinit var firebaseMessaging: FirebaseMessaging
    private lateinit var bookingsBinding: FragmentBookingsBinding
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        bookingsBinding = FragmentBookingsBinding.inflate(inflater, container, false)

        // Initialize Firebase Messaging
//        firebaseMessaging = FirebaseMessaging.getInstance()

        // Get FCM token of currently signed-in user
        val user = FirebaseAuth.getInstance().currentUser
        var userFCMToken: String? = null
        user?.let {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                userFCMToken = token
            }
        }

        // BOOK SERVICE
        bookingsBinding.btnbookService.setOnClickListener {

            if (bookingsBinding.btnservicetypedialog.text.toString() == "Service Type") {
                Toast.makeText(context, "Please Choose Service Type", Toast.LENGTH_SHORT).show()
            } else if (bookingsBinding.btnPickDate.text.toString() == "Service Date") {
                Toast.makeText(context, "Please Choose Service Date", Toast.LENGTH_SHORT).show()
            } else if (bookingsBinding.btnchoosecars.text.toString() == "Choose Car") {
                Toast.makeText(context, "Please Choose Car", Toast.LENGTH_SHORT).show()
            } else {
                val selectedCarOption = bookingsBinding.btnchoosecars.text.toString()
                val selectedServiceDate = bookingsBinding.btnPickDate.text.toString()
                val selectedServiceType = bookingsBinding.btnservicetypedialog.text.toString()


                if (selectedServiceType.isNotEmpty() && selectedServiceDate.isNotEmpty() && selectedCarOption.isNotEmpty()) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val databaseReference =
                            FirebaseDatabase.getInstance().getReference("Requests")
                        val requestId = databaseReference.push().key
                        val request = Requests(
                            requestId,
                            selectedServiceType,
                            selectedServiceDate,
                            selectedCarOption,
                            user.uid
                        )
                        databaseReference.child(requestId!!).setValue(request)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Request booked.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to add request.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please sign in to continue.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please select all the required options.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        // DATE DIALOG(1)
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val currentDate = Calendar.getInstance()

                // Adding 3 months to current date
                currentDate.add(Calendar.MONTH, 3)
                if (selectedDate.after(currentDate)) {
                    Toast.makeText(
                        requireContext(),
                        "Selection beyond 3 months is not allowed",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = sdf.format(selectedDate.time)
                    bookingsBinding.btnPickDate.text = formattedDate
                    // updateDatabase(formattedDate)
                }
            }

        // DATE DIALOG(2)
        bookingsBinding.btnPickDate.setOnClickListener {
            calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            // DISABLE PAST DATES
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // SERVICE TYPE DIALOG (1)
        bookingsBinding.btnservicetypedialog.setOnClickListener {
            showOptionsDialog()
        }

        // CHOOSE CAR (1)
        bookingsBinding.btnchoosecars.setOnClickListener {
            showCarOptions()
        }
        return bookingsBinding.root
    }


    // ------------------------ ALL PRIVATE FUNCTIONS BELOW ---------------------------

    // SERVICE TYPE DIALOG (2)
    private fun showOptionsDialog() {
        val options = resources.getStringArray(R.array.booking_options)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Service Type")
            .setItems(options, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        bookingsBinding.btnservicetypedialog.text = "Premium"
//                        Toast.makeText(requireContext(), "Premium selected ", Toast.LENGTH_SHORT)
//                            .show()
                    }
                    1 -> {
                        bookingsBinding.btnservicetypedialog.text = "Standard"
//                        Toast.makeText(requireContext(), "Standard selected", Toast.LENGTH_SHORT)
//                            .show()
                    }
                    2 -> {
                        bookingsBinding.btnservicetypedialog.text = "Basic"
//                        Toast.makeText(requireContext(), "Basic selected", Toast.LENGTH_SHORT)
//                            .show()
                    }
                }
            }).setNegativeButton("Cancel", null)
        builder.create().show()
    }

    // CHOOSE CAR (2)
    private fun showCarOptions() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_choosecar)
        displayCars(dialog)
        dialog.show()
    }

    // CHOOSE CAR (3)
    private fun displayCars(dialog: Dialog) {
        val rvLayout = dialog.findViewById(R.id.rvLayout) as RecyclerView
        val user = FirebaseAuth.getInstance().currentUser
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Customer").child(user!!.uid).child("Cars")
        val carList = mutableListOf<Car>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (carSnapshot in snapshot.children) {
                    val carBrand = carSnapshot.child("Car_Brand").value as String
                    val carType = carSnapshot.child("Car_Type").value as String
                    val fuelType = carSnapshot.child("Car_Fuel").value as String
                    val carName = carSnapshot.child("Car_Name").value as String
                    val car = Car(carName, carType, fuelType, carBrand)
                    carList.add(car)
                }
                if (carList.isEmpty()) {
                    val noCarsFoundTextView = dialog.findViewById(R.id.nocarsfound) as TextView
                    noCarsFoundTextView.visibility = View.VISIBLE
                    rvLayout.visibility = View.GONE
                } else {
                    val carAdapter = CarAdapter(carList)
                    rvLayout.layoutManager = LinearLayoutManager(requireContext())
                    rvLayout.adapter = carAdapter

                    // Unresolved reference: setOnItemClickListener
                    carAdapter.onItemClickListener = object : CarAdapter.OnItemClickListener {
                        override fun onItemClick(car: Car) {
                            bookingsBinding.btnchoosecars.text = car.Car_Brand
                            dialog.dismiss()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(), "No cars found.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


//    private fun displayCars(dialog: Dialog) {
//        val rvLayout = dialog.findViewById(R.id.rvLayout) as RecyclerView
//        val user = FirebaseAuth.getInstance().currentUser
//        val databaseReference =
//            FirebaseDatabase.getInstance().getReference("Customer").child(user!!.uid).child("Cars")
//        val carList = mutableListOf<Car>()
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (carSnapshot in snapshot.children) {
//                    val carName = carSnapshot.child("Car_Name").value as String
//                    val carModel = carSnapshot.child("Car_Model").value as String
//                    val car = Car(carName, carModel)
//                    carList.add(car)
//                }
//                val carAdapter = CarAdapter(carList)
//                rvLayout.layoutManager = LinearLayoutManager(requireContext())
//                rvLayout.adapter = carAdapter
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireActivity(), "No cars found.", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
