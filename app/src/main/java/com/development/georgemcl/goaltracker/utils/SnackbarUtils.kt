package com.development.georgemcl.goaltracker.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.development.georgemcl.goaltracker.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    @JvmStatic
    fun showSnackbar(message: String, view: View, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view, message, length)
                .setTextColor(ContextCompat.getColor(view.context, R.color.colorWhite)).show()
    }

    @JvmStatic
    fun showSnackbar(message: Int, view: View, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view, message, length)
                .setTextColor(ContextCompat.getColor(view.context, R.color.colorWhite)).show()
    }

    @JvmStatic
    fun showErrorSnackbar(message: String, view: View, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view, message, length)
                .setTextColor(ContextCompat.getColor(view.context, R.color.colorError)).show()
    }

    @JvmStatic
    fun showErrorSnackbar(message: Int, view: View, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view, message, length)
                .setTextColor(ContextCompat.getColor(view.context, R.color.colorError)).show()
    }
}