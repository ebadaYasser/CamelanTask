package com.example.camelantask.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.camelantask.R
import com.example.camelantask.RefreshApi
import com.example.camelantask.home.MainActivity
import com.example.camelantask.utils.Constants.ACTION_PAUSE
import com.example.camelantask.utils.Constants.ACTION_START_OR_RESUME
import com.example.camelantask.utils.Constants.ACTION_STOP
import com.example.camelantask.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.camelantask.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.camelantask.utils.Constants.NOTIFICATION_ID
import com.example.camelantask.utils.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng

class TrackingService : LifecycleService() {
    var isFirstRun = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val points = ArrayList<LatLng>()
        val refreshApi = MutableLiveData<RefreshApi>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        refreshApi.postValue(RefreshApi(false, "", ""))
    }

    private fun addPoint(location: Location?) {
        location?.let {
            val position = LatLng(it.latitude, it.longitude)
            points.apply {
                add(position)
            }
            if (points.size > 1) {
                if (getDistance() >= 500) {
                    refreshApi.postValue(
                        RefreshApi(
                            true,
                            points.last().latitude.toString(),
                            points.last().longitude.toString()
                        )
                    )
                    points.clear()
                } else {
                    refreshApi.postValue(RefreshApi(false, "", ""))
                }
            } else {
                refreshApi.postValue(RefreshApi(false, "", ""))
            }

        }
    }


    private fun getDistance(): Float {
        val locationOne = Location("locationOne")
        val locationTwo = Location("locationTwo")
        locationOne.apply {
            latitude = points[0].latitude
            longitude = points[0].longitude
        }
        locationTwo.apply {
            latitude = points.last().latitude
            longitude = points.last().longitude
        }
        val ws = locationOne.distanceTo(locationTwo)
        return locationOne.distanceTo(locationTwo)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocation(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME -> {
                    if (isFirstRun) {
                        startService()
                        isFirstRun = false
                    } else {
                    }
                }
                ACTION_STOP -> {
                    stopForeground(true)
                    postInitialValues()
                    stopSelf()
                }
                else -> {
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService() {
        isTracking.postValue(true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Camelan")
            .setContentIntent(getMainActivityPendingIntent())
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() =
        PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            FLAG_UPDATE_CURRENT
        )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermission(this)) {
                val request = LocationRequest().apply {
                    interval = 10000L
                    fastestInterval = 5000L
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    LocationCallBack,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(LocationCallBack)
        }
    }

    val LocationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (loc in locations) {
                        addPoint(loc)
                        Log.d("lfnlfknf", "${loc.latitude}  ${loc.longitude}")
                    }
                }
            }
        }
    }
}