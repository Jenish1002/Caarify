package com.carserviceapp.carserviceapp.Adapter

import android.content.Context
import android.graphics.Color.red
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.carserviceapp.carserviceapp.R

class FuelTypeAdapter(context: Context, private val fuelTypes: Array<String>) :
    ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, fuelTypes) {

    override fun isEnabled(position: Int): Boolean {
        // FIRST ITEM NON-CLICKABLE
        return position != 0
    }

    // CHANGES DISPLAY HEADING COLOR
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextColor(ContextCompat.getColor(context, R.color.LightestGray))
        return view
    }

    // CHANGES CONTENT COLOR
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextColor(ContextCompat.getColor(context, R.color.DarkGray))
        return view
    }

}