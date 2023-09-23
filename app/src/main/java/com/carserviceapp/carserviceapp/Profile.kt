package com.carserviceapp.carserviceapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.carserviceapp.carserviceapp.Utils.ProgressBarUtil
import com.carserviceapp.carserviceapp.databinding.FragmentProfileBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class Profile : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentProfileBinding: FragmentProfileBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        fragmentProfileBinding.navigationView.setNavigationItemSelectedListener(this)

        return fragmentProfileBinding.root
    }

    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            Profile().apply {
//
//            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(requireContext(), EditProfile::class.java)
                startActivity(intent)
            }
            R.id.car -> {
                (activity as Dashboard).goToViewCar()
            }

            R.id.settings -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.tv_add_feature_settings),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            R.id.CustomerSupport -> {
                //PROGRESS BAR
                ProgressBarUtil.showProgressDialog(requireActivity())
                val customer_support = getString(R.string.link_customer_support)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(customer_support))
                startActivity(intent)
                ProgressBarUtil.hideProgressDialog()
            }

            R.id.logout -> {
                showLogoutConfirmDialog()
            }
        }
        return true
    }

    private fun showLogoutConfirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout").setMessage(getString(R.string.tv_message_logout))

        builder.setPositiveButton(getString(R.string.tv_yes_logout)) { _, _ ->
            FirebaseAuth.getInstance().signOut()
            activity?.finishAffinity()
        }
        builder.setNegativeButton(getString(R.string.tv_no_logout)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

}

