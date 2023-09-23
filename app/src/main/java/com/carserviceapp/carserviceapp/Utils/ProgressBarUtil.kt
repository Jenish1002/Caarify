package com.carserviceapp.carserviceapp.Utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import com.carserviceapp.carserviceapp.R
import java.util.logging.Handler

//TO SHOW PROGRESS BAR USE : ProgressBarUtil.showProgressDialog(this)

object ProgressBarUtil {
    private var progressDialog: Dialog? = null

    fun showProgressDialog(context: Context) {
        progressDialog = Dialog(context)
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(R.layout.dialog_progressbar)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog?.setCancelable(false)
        progressDialog?.show()

    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }
}