package com.example.camelantask

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.camelantask.extensions.requestPermission

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION) && requestPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }
}