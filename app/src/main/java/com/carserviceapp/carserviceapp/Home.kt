//
package com.carserviceapp.carserviceapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.carserviceapp.carserviceapp.Adapter.CarTypeAdapter
import com.carserviceapp.carserviceapp.Utils.InternetConnectivityUtil
import com.carserviceapp.carserviceapp.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment() {
    private var selectedOilType: String = ""

    val user = FirebaseAuth.getInstance().currentUser
    //private lateinit var supportFragmentManager: FragmentManager
    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("Customer").child(user!!.uid)

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = fragmentHomeBinding.root

        // CHECK INTERNET
        if (!InternetConnectivityUtil.isInternetAvailable(requireContext())
        ) {
            if (container != null) {
                InternetConnectivityUtil.showInternetSnackbar(container, requireContext())
            }
        }

        // OIL CHANGE
        fragmentHomeBinding.clOilchange.setOnClickListener {
         asd("oil",com.carserviceapp.carserviceapp.R.array.oil_types)
        }


        // Replacebattery
        fragmentHomeBinding.clReplacebattery.setOnClickListener {
         asd("awsd",com.carserviceapp.carserviceapp.R.array.battery_types)
        }
        // DISPLAY ADS
        MobileAds.initialize(requireActivity()) {}

        var mAdView = fragmentHomeBinding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        //DISPLAY CURRENT USER'S NAME
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").value.toString()
                    val usernumber = dataSnapshot.child("mobile").value.toString()
                    fragmentHomeBinding.tvhii.text = "Hii, $name"
                    fragmentHomeBinding.tvusernumber.text = " $usernumber"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        // GO TO EDIT PROFILE
        fragmentHomeBinding.imgProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfile::class.java)
            startActivity(intent)
        }

        // GO TO BOOK SERVICE
        fragmentHomeBinding.clScheduleTime.setOnClickListener {
            (activity as Dashboard).goToBooking()
        }

        // OFFERS - CarEngine25
        fragmentHomeBinding.couponCarEngine25.setOnClickListener {
            // Create a custom dialog box
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_carengine25)

            // Set the coupon text
            val couponText = dialog.findViewById<TextView>(R.id.coupon_text)
            //couponText.text = "CARENGINE25"

            // Set the icon click listener to copy the coupon text to clipboard
            val copyIcon = dialog.findViewById<ImageView>(R.id.copy_icon)
            copyIcon.setOnClickListener {
                // Get the clipboard manager
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                // Create a clip with the coupon text
                val clip = ClipData.newPlainText("Coupon", couponText.text)

                // Set the clip on the clipboard
                clipboard.setPrimaryClip(clip)

                // Show a toast message indicating that the coupon has been copied
                Toast.makeText(requireContext(), "Coupon copied", Toast.LENGTH_SHORT).show()

                // Dismiss the dialog box
                dialog.dismiss()
            }

            // Show the dialog box
            dialog.show()
        }

        // OFFERS - CarOiling15
        fragmentHomeBinding.couponCarOiling15.setOnClickListener {
            // Create a custom dialog box
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_caroiling15)

            // Set the coupon text
            val couponText = dialog.findViewById<TextView>(R.id.coupon_text)

            // Set the icon click listener to copy the coupon text to clipboard
            val copyIcon = dialog.findViewById<ImageView>(R.id.copy_icon)
            copyIcon.setOnClickListener {
                // Get the clipboard manager
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                // Create a clip with the coupon text
                val clip = ClipData.newPlainText("Coupon", couponText.text)

                // Set the clip on the clipboard
                clipboard.setPrimaryClip(clip)

                // Show a toast message indicating that the coupon has been copied
                Toast.makeText(requireContext(), "Coupon copied", Toast.LENGTH_SHORT).show()

                // Dismiss the dialog box
                dialog.dismiss()
            }

            // Show the dialog box
            dialog.show()
        }

        // OFFERS - CarTyre25
        fragmentHomeBinding.couponCarTyre25.setOnClickListener {
            // Create a custom dialog box
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_cartyre25)

            // Set the coupon text
            val couponText = dialog.findViewById<TextView>(R.id.coupon_text)

            // Set the icon click listener to copy the coupon text to clipboard
            val copyIcon = dialog.findViewById<ImageView>(R.id.copy_icon)
            copyIcon.setOnClickListener {
                // Get the clipboard manager
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                // Create a clip with the coupon text
                val clip = ClipData.newPlainText("Coupon", couponText.text)

                // Set the clip on the clipboard
                clipboard.setPrimaryClip(clip)

                // Show a toast message indicating that the coupon has been copied
                Toast.makeText(requireContext(), "Coupon copied", Toast.LENGTH_SHORT).show()

                // Dismiss the dialog box
                dialog.dismiss()
            }

            // Show the dialog box
            dialog.show()
        }

        // OFFERS - CarWash1500
        fragmentHomeBinding.couponCarWash1500.setOnClickListener {
            // Create a custom dialog box
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_carwash1500)

            // Set the coupon text
            val couponText = dialog.findViewById<TextView>(R.id.coupon_text)

            // Set the icon click listener to copy the coupon text to clipboard
            val copyIcon = dialog.findViewById<ImageView>(R.id.copy_icon)
            copyIcon.setOnClickListener {
                // Get the clipboard manager
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                // Create a clip with the coupon text
                val clip = ClipData.newPlainText("Coupon", couponText.text)

                // Set the clip on the clipboard
                clipboard.setPrimaryClip(clip)

                // Show a toast message indicating that the coupon has been copied
                Toast.makeText(requireContext(), "Coupon copied", Toast.LENGTH_SHORT).show()

                // Dismiss the dialog box
                dialog.dismiss()
            }

            // Show the dialog box
            dialog.show()
        }

        // BASIC PACKAGE
        fragmentHomeBinding.clBasicpkcg.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.popup_basicservice)
            dialog.setCanceledOnTouchOutside(false)
            val closeButton = dialog.findViewById<Button>(R.id.btn_closebasic)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        // STANDARD PACKAGE
        fragmentHomeBinding.clStandardckg.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.popup_standardservice)
            dialog.setCanceledOnTouchOutside(false)
            val closeButton = dialog.findViewById<Button>(R.id.btn_closestandard)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        // PREMIUM PACKAGE
        fragmentHomeBinding.clPremiumpckg.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.popup_premiumservice)
            dialog.setCanceledOnTouchOutside(false)
            val closeButton = dialog.findViewById<Button>(R.id.btn_closepremium)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        return view
    }

    private fun asd(miniservicename: String, servicetype: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.activity_mini_service)

        val spinner = dialog.findViewById<Spinner>(R.id.spinner_OilType)
        val oilTypes = resources.getStringArray(servicetype)
        val adapter_cartype = CarTypeAdapter(requireContext(), oilTypes)
        spinner.setAdapter(adapter_cartype);
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (position > 0) {
                        selectedOilType = oilTypes[position]
                        Toast.makeText(
                            requireContext(),
                            "$miniservicename: $selectedOilType",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
        dialog.show()
    }
}

