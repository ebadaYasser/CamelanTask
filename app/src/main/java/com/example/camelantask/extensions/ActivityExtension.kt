package com.example.camelantask.extensions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


fun AppCompatActivity.requestPermission(permission: String): Boolean {
    return if (checkPermission(permission, this)) {
        true
    } else {
        requestPermission(permission, this)
        false
    }
}

private fun checkPermission(permission: String, activity: Activity): Boolean {
    return ContextCompat.checkSelfPermission(
        activity,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

private fun requestPermission(permission: String, activity: Activity) {
    if (permission == Manifest.permission.ACCESS_COARSE_LOCATION || permission == Manifest.permission.ACCESS_FINE_LOCATION) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), 600)
    } else
        ActivityCompat.requestPermissions(activity, arrayOf(permission), 601)


}

fun AppCompatActivity.applyToolbar(toolbar: Toolbar, title: String? = null) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setTitle(title)
    }
}