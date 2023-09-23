//

package com.carserviceapp.carserviceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carserviceapp.carserviceapp.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {

    private lateinit var dashboardBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.FLayout, Home())
            .commit()

        dashboardBinding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLayout, Home())
                        .commit()
                    true
                }
                R.id.bookings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLayout, Bookings())
                        .commit()
                    true
                }
                R.id.share_location -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLayout, ShareLocation())
                        .commit()
                    true
                }
                R.id.mycar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLayout, MyCars())
                        .commit()
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLayout, Profile())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    public fun goToBooking(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.FLayout, Bookings())
            .commit()
    }

    public fun goToViewCar(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.FLayout, MyCars())
            .commit()
    }
}