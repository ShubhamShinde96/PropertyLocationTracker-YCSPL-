package com.shubham.ycsplassignment.utils

import android.app.Activity
import android.content.res.Resources

class Helper {

    companion object {

        fun getSoftButtonsBarSizePort(activity: Activity): Int {

            val resources: Resources = activity.resources
            val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                resources.getDimensionPixelSize(resourceId)
            } else 0

        }
    }
}