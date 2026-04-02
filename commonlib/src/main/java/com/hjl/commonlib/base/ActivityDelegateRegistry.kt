package com.hjl.commonlib.base

import android.app.Activity
import android.content.Context

/**
 * Cross-module activity hooks.
 *
 * Keep the base activity layer free from reverse dependencies on feature/app modules.
 */
interface ActivityDelegate {
    fun attachBaseContext(base: Context): Context = base

    fun onRequestPermissionsResult(activity: Activity) {
    }
}

object ActivityDelegateRegistry {
    @Volatile
    var delegate: ActivityDelegate? = null

    @JvmStatic
    fun attachBaseContext(base: Context?): Context? {
        if (base == null) {
            return null
        }
        return delegate?.attachBaseContext(base) ?: base
    }

    @JvmStatic
    fun onRequestPermissionsResult(activity: Activity) {
        delegate?.onRequestPermissionsResult(activity)
    }
}
