//
package com.carserviceapp.carserviceapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.carserviceapp.carserviceapp.Utils.InternetConnectivityUtil
import com.carserviceapp.carserviceapp.databinding.FragmentSharelocationBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ShareLocation : Fragment(), OnMapReadyCallback {

    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var PERMISSION_ID = 1001
    private lateinit var sharelocationBinding: FragmentSharelocationBinding
    private lateinit var mMap: GoogleMap
    private var userLocationMarker: Marker? = null
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        sharelocationBinding = FragmentSharelocationBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        database = Firebase.database.reference

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        // CHECK INTERNET
        if (!InternetConnectivityUtil.isInternetAvailable(requireContext())) {
            if (container != null) {
                InternetConnectivityUtil.showInternetSnackbar(container, requireContext())
            }
        }

        sharelocationBinding.btnShareLocation.setOnClickListener {
            getLastLocation()
        }

        return sharelocationBinding.root
    }

    // HOW TO UPDATE USERS LOCATION IF THE EMAIL ID OF CURRENT USER ALREADY EXISTS THEN UPDATE THE CURRENT LOCATION

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        val address = getMaxAddress(location.latitude, location.longitude)

                        // SHOW DIALOG BOX WITH SHARE AND CANCEL OPTIONS
                        AlertDialog.Builder(requireContext())
                            .setTitle("Share Your Location?")
                            .setMessage(address)
                            .setPositiveButton("Share") { dialog, which ->

                                val currentUser = Firebase.auth.currentUser
                                if (currentUser != null) {
                                    val uid = currentUser.uid
                                    val email = currentUser.email
                                    val location = address

                                    val emergencyLocation = hashMapOf(
                                        "Email" to email,
                                        "Location" to location
                                    )

                                    // STORES THE USER'S CURRENT LOCATION UNDER THE PATH OF "EMERGENCY LOCATION" WITH A NEW ID
                                    val newId = database.child("Emergency Location").push().key
                                    database.child("Emergency Location").child(newId!!)
                                        .setValue(emergencyLocation)

                                    // UPDATE CURRENT USER'S LOCATION DATA
                                    val userLocation = hashMapOf(
                                        "Location" to location
                                    )
//                                    val userRef = database.child("Users").child(uid)
//                                        .updateChildren(userLocation as Map<String, Any>)
                                }

                                // TOAST TO SHOW THE LOCATION HAS BEEN SHARED
                                Toast.makeText(
                                    requireContext(),
                                    "Your location has been shared.",
                                    Toast.LENGTH_LONG
                                ).show()

                                // LOCATION MARKER
                                val userLatLng = LatLng(location.latitude, location.longitude)
                                if (userLocationMarker != null) {
                                    userLocationMarker?.remove()
                                }

                                // ICON TO MARK USERS CURRENT LOCATION
                                val iconBitmap = BitmapFactory.decodeResource(resources,
                                    R.mipmap.thelocationpin2)
                                val icon = BitmapDescriptorFactory.fromBitmap(iconBitmap)

                                userLocationMarker = mMap.addMarker(
                                    MarkerOptions()
                                        .position(userLatLng)
                                        .title(address)
                                        .icon(icon)
                                )
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,
                                    15f))

                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Please enable your location", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            requestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )

    }

    private fun isLocationEnabled(): Boolean {

        var locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        // var locationManager: LocationManager? = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // ADDRESS LINE
    private fun getMaxAddress(lat: Double, long: Double): String {
        var maxadd = ""
        var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
        var addresses = geoCoder.getFromLocation(lat, long, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            var maxAddressLineIndex = addresses[0].maxAddressLineIndex
            if (maxAddressLineIndex >= 0) {
                maxadd = addresses[0].getAddressLine(maxAddressLineIndex)
            } else {
                maxadd = "No address lines found"
            }
        } else {
            maxadd = "Address not found"
        }

        return maxadd
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "You have the permission")
            }
        }
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}


// ------------------------------------ PREMISES ------------------------------------
//    private fun getPremises(lat: Double, long: Double) {
//        var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
//        var addresses = geoCoder.getFromLocation(lat, long, 1)
//        if (addresses != null && addresses.isNotEmpty()) {
//            var premises = addresses[0].premises
//            if (!premises.isNullOrEmpty()) {
//                Toast.makeText(requireActivity(), "Premises: $premises", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireActivity(), "No premises found", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(requireActivity(), "Address not found", Toast.LENGTH_SHORT).show()
//        }
//    }

// ------------------------------------ POPULAR PLACE AROUND ------------------------------------
//private fun getFeature(lat: Double, long: Double): String {
//    var addline = ""
//    var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
//    var address = geoCoder.getFromLocation(lat, long, 1)
//    addline = address?.get(0)!!.featureName // RETURNS POPULAR AROUND PLACE
//
//    return addline
//}

// ------------------------------------ AREA NAME ------------------------------------
//private fun getAreaName(lat: Double, long: Double): String {
//    var areaName = ""
//    var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
//    var address = geoCoder.getFromLocation(lat, long, 1)
//    areaName = address?.get(0)!!.subLocality // RETURNS AREA NAME
//
//    return areaName
//}

//------------------------------------ CITY NAME ------------------------------------
//private fun getCityName(lat: Double, long: Double): String {
//    var cityName = ""
//    var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
//    var address = geoCoder.getFromLocation(lat, long, 1)
//    cityName = address?.get(0)!!.locality // RETURNS CITY NAME
//
//    return cityName
//}

// ------------------------------------ STATE ------------------------------------
//private fun getStateName(lat: Double, long: Double): String {
//    var statename = ""
//    var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
//    var address = geoCoder.getFromLocation(lat, long, 1)
//    statename = address?.get(0)!!.adminArea // RETURNS STATE NAME
//
//    return statename
//}

// ------------------------------------ PIN CODE ------------------------------------
//private fun getPinCode(lat: Double, long: Double): String {
//    var pincode = ""
//    var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
//    var address = geoCoder.getFromLocation(lat, long, 1)
//    pincode = address?.get(0)!!.postalCode // RETURNS PINCODE
//    //cityName = address?.get(0)!!.subLocality
//
//    return pincode
//}

// ------------------------------------ COUNTRY NAME ------------------------------------
//private fun getCountryName(lat: Double, long: Double): String {
//    var countryName = ""
//    var geoCoder = Geocoder(requireActivity(), Locale.getDefault())
//    var address = geoCoder.getFromLocation(lat, long, 1)
//    countryName = address?.get(0)!!.countryName // RETURNS COUNTRY NAME
//
//    return countryName
//}

// ------------------------------------ LOCATION SHARING WITHOUT DIALOG BOX ------------------------------------
//    private fun getLastLocation() {
//        if (checkPermission()) {
//            if (isLocationEnabled()) {
//                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
//                    var location: Location? = task.result
//                    if (location == null) {
//                        getNewLocation()
//                    } else {
//                        // TOAST USERS CURRENT LOCATION
//                        Toast.makeText(requireContext(),
//                            //"Your coordinates are: " + location.latitude + "  " + location.longitude +"\n"+
//                            " " + getMaxAddress(location.latitude, location.longitude) + "\n",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        // STORING EMERGENCY LOCATION TO FIREBASE - OVERWRITES LOCATION EVERY TIME USER SHARES LATEST LOCATION
//                        val currentUser = Firebase.auth.currentUser
//                        if (currentUser != null) {
//                            val uid = currentUser.uid
//                            val userRef = database.child("Customer").child(uid)
//                                .child("EmergencyLocation/location")
//                                .setValue(getMaxAddress(location.latitude, location.longitude))
//                        }
//
//                        // TOAST TO SHOW THE LOCATION HAS BEEN SHARED
//                        Toast.makeText(
//                            requireContext(),
//                            "Your location has been shared.",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        // LOCATION MARKER
//                        val userLatLng = LatLng(location.latitude, location.longitude)
//                        if (userLocationMarker != null) {
//                            userLocationMarker?.remove()
//                        }
//
//                        // ICON TO MARK USERS CURRENT LOCATION
//                        val iconBitmap = BitmapFactory.decodeResource(resources, R.mipmap.thelocationpin2)
//                        val icon = BitmapDescriptorFactory.fromBitmap(iconBitmap)
//
//                        userLocationMarker = mMap.addMarker(
//                            MarkerOptions()
//                                .position(userLatLng)
//                                .title(getMaxAddress(location.latitude, location.longitude))
//                                .icon(icon)
//                        )
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
//                    }
//                }
//            } else {
//                Toast.makeText(requireActivity(), "Please enable your location", Toast.LENGTH_LONG)
//                    .show()
//            }
//        } else {
//            requestPermission()
//        }
//    }

//------------------------------------ TO SHOW STATIC LOCATION WITH MARKER----------------------------------------

//class ShareLocation : Fragment(), OnMapReadyCallback {
//
//    private lateinit var mMap: GoogleMap
//    private lateinit var sharelocationBinding: FragmentSharelocationBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        sharelocationBinding = FragmentSharelocationBinding.inflate(inflater, container, false)
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        // CHECK INTERNET
//        if (!InternetConnectivityUtil.isInternetAvailable(requireContext())
//        ) {
//            if (container != null) {
//                InternetConnectivityUtil.showInternetSnackbar(container, requireContext())
//            }
//        }
//
//        // when user clicks on share location button add a segment called"Location" in current users id which stores link/ latitudelongitude of current users location to firebase
//        sharelocationBinding.btnsharelocation.setOnClickListener {
//            //Toast.makeText(requireActivity(), "Location sent ", Toast.LENGTH_SHORT).show()
//        }
//
//        return sharelocationBinding.root
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val ahmedabad = LatLng(23.022505, 72.571365)
//        mMap.addMarker(MarkerOptions().position(ahmedabad).title("User in Ahmedabad"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ahmedabad))
//    }
//}