package com.carserviceapp.carserviceapp.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.carserviceapp.carserviceapp.R

class CarBrandAdapter(context: Context, private val carTypes: Array<String>) :
    ArrayAdapter<String>(context,
        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
        carTypes) {

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

// DIALOG ADAPTER ORIGINAL
//class CarBrandAdapter(val ctn: Context?, private val carBrands: List<CarBrand>) :
//    RecyclerView.Adapter<CarBrandAdapter.ViewHolder>() {
//    private lateinit var sharedPreferences: SharedPreferences
//    private var listener: CarBrandDialog.OnCarBrandSelectedListener? = null
//
//    fun setListener(listener: CarBrandDialog.OnCarBrandSelectedListener?) {
//        this.listener = listener
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val carLogo: ImageView = itemView.findViewById(R.id.iv_CarBrandLogo)
//        val carName: TextView = itemView.findViewById(R.id.tv_CarBrandName)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.items_carbrand, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val carBrand = carBrands[position]
//        holder.carLogo.setImageResource(carBrand.logoResId)
//        holder.carName.text = carBrand.name
//        holder.itemView.setOnClickListener {
//            listener?.onCarBrandSelected(carBrand)
//            // val selectedCarBrand = carBrands[position]
//            Toast.makeText(holder.itemView.context, " ${carBrand.name}", Toast.LENGTH_LONG)
//                .show()
//            val intent = Intent(ctn,AddCar::class.java)
//            val sharedPreferences = ctn?.getSharedPreferences("share", Context.MODE_PRIVATE)
//            sharedPreferences?.edit()!!.putString("Car_Brand", carBrand.name).commit()
//            intent.putExtra("Car_Brand",carBrand.name)
////            ctn?.startActivity(intent)
//            sharedPreferences.edit().remove("Car_Brand")
//
//        }
//    }
//
//    override fun getItemCount() = carBrands.size
//}



