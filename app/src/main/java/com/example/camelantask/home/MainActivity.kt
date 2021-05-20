package com.example.camelantask.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.camelantask.BaseActivity
import com.example.camelantask.R
import com.example.camelantask.RefreshApi
import com.example.camelantask.common.CommonState
import com.example.camelantask.entities.images.ImageResponse
import com.example.camelantask.entities.places.PlacesResponse
import com.example.camelantask.extensions.applyToolbar
import com.example.camelantask.extensions.gone
import com.example.camelantask.extensions.requestPermission
import com.example.camelantask.extensions.visible
import com.example.camelantask.home.adapters.PlaceAdapter
import com.example.camelantask.home.viewmodels.HomeViewModel
import com.example.camelantask.services.TrackingService
import com.example.camelantask.utils.Constants.ACTION_START_OR_RESUME
import com.example.camelantask.utils.Constants.ACTION_STOP
import com.example.camelantask.utils.PrefManager
import com.example.camelantask.utils.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val viewModel: HomeViewModel by viewModel()
    private val prefManager: PrefManager by inject()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var refreshApi: RefreshApi = RefreshApi(false, "", "")
    private val placeAdapter: PlaceAdapter by lazy { PlaceAdapter() }

    // life cycle region
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applyToolbar(toolbar, getString(R.string.near_by))
        initModelObserver()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()
        rvPlaces.adapter = placeAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_mode)
        menuItem.title =
            if (prefManager.isSingleMode()) getString(R.string.real_time) else getString(R.string.single_update)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_mode -> {
                if (prefManager.isSingleMode()) {
                    sendActionToService(ACTION_START_OR_RESUME)
                    prefManager.setSingleMode(false)
                    item.title = getString(R.string.single_update)
                } else {
                    sendActionToService(ACTION_STOP)
                    prefManager.setSingleMode(true)
                    item.title = getString(R.string.real_time)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // end region

    // helper functions region
    private fun initModelObserver() {
        viewModel.apply {
            observeVeStates(this@MainActivity, Observer {
                handleGetPlaces(it)
            })
        }
        viewModel.apply {
            observeImageState(this@MainActivity, Observer {
                handleGetImage(it)
            })
        }
        TrackingService.refreshApi.observe(this@MainActivity, {
            if (it.refresh) {
                refreshApi.lat = it.lat
                refreshApi.long = it.long
                val latLong = it.lat + "," + it.long
                viewModel.getPlaces(latLong, 200)
            } else {
                refreshApi.refresh = false
                refreshApi.lat = ""
                refreshApi.long = ""
            }
        })
    }

    private fun handleGetPlaces(state: CommonState<PlacesResponse>) {
        when (state) {
            is CommonState.ShowLoading -> {
                rvPlaces.gone()
                loadingView.visible()

            }
            is CommonState.LoadingFinished -> {
                loadingView.gone()

            }
            is CommonState.Success -> {
                placeAdapter.items.clear()
                if (state.data.response?.groups?.get(0)?.items?.isEmpty() == true) {
                    rvPlaces.gone()
                    loadingView.gone()
                    noInternNetView.gone()
                    noData.visible()
                    errorView.gone()
                } else {
                    rvPlaces.visible()
                    loadingView.gone()
                    noInternNetView.gone()
                    noData.gone()
                    errorView.gone()
                }
                state.data.response?.groups?.get(0)?.items?.let {
                    placeAdapter.addItems(it)
                }
            }
            is CommonState.Error -> {
                loadingView.gone()
                noInternNetView.gone()
                noData.gone()
                errorView.visible()
            }
            else -> {
            }
        }
    }

    private fun handleGetImage(state: CommonState<ImageResponse>) {
        when (state) {
            is CommonState.ShowLoading -> {
                loadingView.visible()

            }
            is CommonState.LoadingFinished -> {
                loadingView.gone()

            }
            is CommonState.Success -> {
                rvPlaces.visible()
                loadingView.gone()
                noInternNetView.gone()
                noData.gone()
                errorView.gone()

            }
            is CommonState.Error -> {
                loadingView.gone()
                noInternNetView.gone()
                noData.gone()
                errorView.visible()
            }
            else -> {
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun lastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latLong =
                    if (refreshApi.lat.isNotEmpty() && refreshApi.long.isNotEmpty()) refreshApi.lat.toString() + "," + refreshApi.long.toString() else it.latitude.toString() + "," + it.longitude.toString()
                viewModel.getPlaces(latLong, 200)
            }
        }
    }

    private fun requestPermission() {
        if (TrackingUtility.hasLocationPermission(this)) {
            lastKnownLocation()
            if (!prefManager.isSingleMode())
                sendActionToService(ACTION_START_OR_RESUME)


        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permission",
                    101,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permission",
                    101,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }

    private fun sendActionToService(actionService: String) =
        Intent(this, TrackingService::class.java).also {
            it.action = actionService
            startService(it)
        }

    // end region

    // callBacks region
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        lastKnownLocation()
        if (!prefManager.isSingleMode())
            sendActionToService(ACTION_START_OR_RESUME)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // end region

}