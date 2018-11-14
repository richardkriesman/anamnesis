package com.team4.anamnesis.utils

import android.content.Context

object ScreenUtils {

    /**
     * Converts a px size to the correct dp value for this device.
     */
    fun pxToDp(context: Context, px: Int): Int {
        val density: Double = context.resources.displayMetrics.density.toDouble()
        return Math.ceil(px / density).toInt()
    }

    /**
     * Converts a dp size to the correct px value for this device.
     */
    fun dpToPx(context: Context, dp: Int): Int {
        val density: Double = context.resources.displayMetrics.density.toDouble()
        return Math.ceil(dp * density).toInt()
    }

}