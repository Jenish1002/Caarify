package com.carserviceapp.carserviceapp

// ORIGINAL WORKING
//class CarBrandDialog(private val carBrands: List<CarBrand>) : DialogFragment() {
//
//    interface OnCarBrandSelectedListener {
//        fun onCarBrandSelected(carBrand: CarBrand)
//    }
//
//    private var listener: OnCarBrandSelectedListener? = null
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        listener = context as? OnCarBrandSelectedListener
//        if (listener == null) {
//            throw ClassCastException("$context must implement OnCarBrandSelectedListener")
//        }
//    }
//
//    // original
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val view = LayoutInflater.from(context).inflate(R.layout.dialog_carbrand, null)
//        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_carbrandList)
//        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
//        val adapter = CarBrandAdapter(context,carBrands)
//        recyclerView.adapter = adapter
//
//        val dialog = Dialog(requireContext())
//        dialog.setContentView(view)
//
//        return dialog
//    }
//}

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(requireActivity())
//        val inflater = requireActivity().layoutInflater
//        val dialogView = inflater.inflate(R.layout.dialog_carbrand, null)
//        val rv = dialogView.findViewById<RecyclerView>(R.id.rv_carbrandList)
//        val adapter = CarBrandAdapter(carBrands)
//        rv.adapter = adapter
//        rv.setOnItemClickListener { parent, view, position, id ->
//            val selectedCarBrand = carBrands[position]
//            listener?.onCarBrandSelected(selectedCarBrand)
//            dismiss()
//        }
//        builder.setView(dialogView)
//        return builder.create()
//    }
//}

//        // Get the display dimensions
//        val displayMetrics = DisplayMetrics()
//        dialog.window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
//        val displayWidth = displayMetrics.widthPixels
//        val displayHeight = displayMetrics.heightPixels
//
//        // Set the dialog width and height to be 80% of the screen width and height
//        val layoutParams = WindowManager.LayoutParams()
//        layoutParams.copyFrom(dialog.window?.attributes)
//        val dialogWidth = (displayWidth * 0.8).toInt()
//        val dialogHeight = (displayHeight * 0.8).toInt()
//        layoutParams.width = dialogWidth
//        layoutParams.height = dialogHeight
//        dialog.window?.attributes = layoutParams