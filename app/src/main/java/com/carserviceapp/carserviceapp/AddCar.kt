package com.carserviceapp.carserviceapp

//noinspection SuspiciousImport

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carserviceapp.carserviceapp.Adapter.CarBrandAdapter
import com.carserviceapp.carserviceapp.Adapter.CarTypeAdapter
import com.carserviceapp.carserviceapp.Adapter.FuelTypeAdapter
import com.carserviceapp.carserviceapp.Utils.InternetConnectivityUtil
import com.carserviceapp.carserviceapp.databinding.ActivityAddcarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*


@Suppress("DEPRECATION")
//

class AddCar : AppCompatActivity() {

    // private lateinit var carBrandDialog: CarBrandDialog
    //private lateinit var carBrands: List<CarBrand>
    private lateinit var addcarBinding: ActivityAddcarBinding
    val databaseReference = FirebaseDatabase.getInstance().reference
    val user = FirebaseAuth.getInstance().currentUser
    private var selectedFuelType: String = ""
    private var selectedCarType: String = ""
    private var selectedCarBrand: String = ""
    //  private var carname: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addcarBinding = ActivityAddcarBinding.inflate(layoutInflater)
        setContentView(addcarBinding.root)
//        listOf(
////             for sublist
////            CarBrand(R.mipmap.car_volkswagen, "VW", listOf("Golf", "Passat", "Tiguan")),
//            CarBrand(R.mipmap.car_volkswagen, getString(R.string.option_volkswagen_addcar)),
//            CarBrand(R.mipmap.car_suzuki, getString(R.string.option_suzuki_addcar)),
//            CarBrand(R.mipmap.car_skoda, getString(R.string.option_skoda_addcar)),
//            CarBrand(R.mipmap.car_tata, getString(R.string.option_tata_addcar)),
//
//            CarBrand(R.mipmap.car_mercedes, getString(R.string.option_mercedes_addcar)),
//            CarBrand(R.mipmap.car_jaguar, getString(R.string.option_jaguar_addcar)),
//            CarBrand(R.mipmap.car_honda, getString(R.string.option_honda_addcar)),
//            CarBrand(R.mipmap.car_fiat, getString(R.string.option_fiat_addcar)),
//
//            CarBrand(R.mipmap.car_nisaan, getString(R.string.option_nisaan_addcar)),
//            CarBrand(R.mipmap.car_hyundai, getString(R.string.option_hyundai_addcar)),
//            CarBrand(R.mipmap.car_mazda, getString(R.string.option_mazda_addcar)),
//            CarBrand(R.mipmap.car_kia, getString(R.string.option_kia_addcar)),
//
//            CarBrand(R.mipmap.car_renault, getString(R.string.option_renault_addcar)),
//            CarBrand(R.mipmap.car_toyota, getString(R.string.option_toyota_addcar)),
//            CarBrand(R.mipmap.car_ford, getString(R.string.option_ford_addcar)),
//            CarBrand(R.mipmap.car_audi, getString(R.string.option_audi_addcar)),
//
//            CarBrand(R.mipmap.car_rollsroyce, getString(R.string.option_rollsroyce_addcar)),
//            CarBrand(R.mipmap.car_datsun, getString(R.string.option_datsun_addcar)),
//            CarBrand(R.mipmap.car_volvo, getString(R.string.option_volvo_addcar)),
//            CarBrand(R.mipmap.car_bmw, getString(R.string.option_bmw_addcar)),
//
//            CarBrand(R.mipmap.car_mahindra, getString(R.string.option_mahindra_addcar)),
//            CarBrand(R.mipmap.car_premier, getString(R.string.option_premier_addcar)),
//            CarBrand(R.mipmap.car_buick, getString(R.string.option_buick_addcar)),
//            CarBrand(R.mipmap.car_jeep, getString(R.string.option_jeep_addcar)),
//
//            ).also { carBrands = it }
//        carBrandDialog = CarBrandDialog(carBrands)

        // CAR BRAND SELECTION

        /* var shar = getSharedPreferences("share", MODE_PRIVATE)
         var mytext = shar.getString("Car_Brand", "defalut").toString()
         addcarBinding.btnCarBrand.text = mytext*/

        /* addcarBinding.spinnerCarBrand.setOnClickListener {

         }*/

        addcarBinding.btnCancel.setOnClickListener {
            super.onBackPressed()
        }
        addcarBinding.btnSave.setOnClickListener {
            addCarDetails()
        }

        // CAR TYPE ADAPTER
        val carTypes = resources.getStringArray(com.carserviceapp.carserviceapp.R.array.car_types)
        val adapter_cartype = CarTypeAdapter(this, carTypes)
        addcarBinding.spinnerCarType.adapter = adapter_cartype

        addcarBinding.spinnerCarType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position > 0) {
                        selectedCarType = carTypes[position]
                        Toast.makeText(
                            this@AddCar,
                            "Car type: $selectedCarType",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

        // CAR FUEL TYPE ADAPTER
        val fuelTypes = resources.getStringArray(com.carserviceapp.carserviceapp.R.array.fuel_types)
        val adapter_fueltype = FuelTypeAdapter(this, fuelTypes)
        addcarBinding.spinnerFuelType.adapter = adapter_fueltype
        addcarBinding.spinnerFuelType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position > 0) {
                        selectedFuelType = fuelTypes[position]
                        Toast.makeText(
                            this@AddCar,
                            "Fuel type: $selectedFuelType",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

        // CAR BRAND ADAPTER
        val brandtypes =
            resources.getStringArray(com.carserviceapp.carserviceapp.R.array.car_brands)
        val adapter_brands = CarBrandAdapter(this, brandtypes)
        addcarBinding.spinnerCarBrand.adapter = adapter_brands
        addcarBinding.spinnerCarBrand.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position > 0) {
                        selectedCarBrand = brandtypes[position]
                        Toast.makeText(
                            this@AddCar,
                            "Car Brand: $selectedCarBrand",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

        // CHECK INTERNET
        val parentLayout = findViewById<View>(android.R.id.content)
        if (!InternetConnectivityUtil.isInternetAvailable(this)) {
            if (parentLayout != null) {
                InternetConnectivityUtil.showInternetSnackbar(parentLayout, this)
            }
        }
    }


    private fun addCarDetails() {
        val carnumber = addcarBinding.etcarnumber.text.toString()
        if (!isValidIndiaCarNumberPlate(carnumber)) {
            Toast.makeText(this, "Enter valid car number.", Toast.LENGTH_LONG).show()
            return
        }
        if (selectedCarBrand.isEmpty()) {
            Toast.makeText(this, "Please select a car brand", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedFuelType.isEmpty()) {
            Toast.makeText(this, "Please select a fuel type", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedCarType.isEmpty()) {
            Toast.makeText(this, "Please select a car type", Toast.LENGTH_SHORT).show()
            return
        } else {

            // Generate ID
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val carId = databaseReference.child("Customers").child(userId).child("Cars").push().key

            // Create car data
            val carData = HashMap<String, Any>()
            val carnumber = addcarBinding.etcarnumber.text.toString()
            carData["Car_Name"] = carnumber
            carData["Car_Brand"] = selectedCarBrand
            carData["Car_Fuel"] = selectedFuelType
            carData["Car_Type"] = selectedCarType

            carId?.let { id ->
                databaseReference.child("Customer").child(userId).child("Cars").child(id)
                    .setValue(carData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Car added", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        } else {
                            Toast.makeText(
                                this,
                                "Unable to add car! \nTry again later.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }
//        shar.edit().remove("Car_Brand").apply()

    }

    private fun isValidIndiaCarNumberPlate(input: String): Boolean {
        val indiaCarNumberPlatePattern = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$"
        return input.matches(indiaCarNumberPlatePattern.toRegex())
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /*  override fun onCarBrandSelected(carBrand: CarBrand) {
          carBrandDialog.dismiss()
          var shar = getSharedPreferences("share", MODE_PRIVATE)

          var mytext = shar.getString("Car_Brand", "defalut").toString()
          addcarBinding.btnCarBrand.text = mytext
      }*/
}


// CAR VALIDATION

//private fun isValidIndiaCarNumberPlate(input: String): Boolean {
//    val indiaCarNumberPlatePattern = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$"
//    return input.matches(indiaCarNumberPlatePattern.toRegex())
//}

// ADDING DATA TO DATABASE - PREVIOUS CODE

//        val carname = addcarBinding.etCarName.text.toString()
//        val carbrand = addcarBinding.etcarbrand.text.toString()
//        val carmodel = addcarBinding.etcarmodel.text.toString()
//        val carnumber = addcarBinding.etcarnumber.text.toString()
//        val cartype = addcarBinding.etCarType.text.toString()
//        val carfuel = addcarBinding.etcarFuelType.text.toString()
//
//        if (carname.isEmpty() || carbrand.isEmpty() || carmodel.isEmpty() || carnumber.isEmpty() || cartype.isEmpty() || carfuel.isEmpty()) {
//            Toast.makeText(this, "Fields must not be empty.", Toast.LENGTH_LONG).show()
//            return
//        }
//        if (carmodel.length < 4) {
//            Toast.makeText(this, "Enter year of car model", Toast.LENGTH_LONG).show()
//            return
//        }
//        if (!isValidIndiaCarNumberPlate(carnumber)) {
//            Toast.makeText(this, "Enter valid car number.", Toast.LENGTH_LONG).show()
//            return
//        }
//
//        val databaseReference =
//            FirebaseDatabase.getInstance().getReference("Customer").child(user!!.uid)
//        // generate auto-incrementing car ID
//        val carId = databaseReference.child("Cars").push().key!!
//
//        val carData = HashMap<String, Any>()
//        carData["Car_Name"] = carname
//        carData["Car_Brand"] = carbrand
//        carData["Car_Model"] = carmodel
//        carData["Car_Number"] = carnumber
//        carData["Car_Type"] = cartype
//        carData["Car_Fuel"] = carfuel
//
//        databaseReference.child("Cars").child(carId).setValue(carData)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Car added", Toast.LENGTH_SHORT).show()
//                    onBackPressed()
//                } else {
//                    Toast.makeText(this, "Something went wrong! Try Again.", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }