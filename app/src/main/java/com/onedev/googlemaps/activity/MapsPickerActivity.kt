package com.onedev.googlemaps.activity

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.onedev.googlemaps.R
import com.onedev.googlemaps.databinding.ActivityMapsPickerBinding
import com.onedev.googlemaps.manager.LocationManager
import com.onedev.googlemaps.utils.BaseActivityBinding
import com.onedev.googlemaps.utils.toLatLng

class MapsPickerActivity : BaseActivityBinding<ActivityMapsPickerBinding>() {

    private var showPanel = true

    private val locationManager by lazy { LocationManager(this) }

    override fun inflateBinding(): ActivityMapsPickerBinding {
        return ActivityMapsPickerBinding.inflate(layoutInflater)
    }

    override fun onCreateBinding(savedInstanceState: Bundle?) {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReady())
    }

    private fun onMapReady(): OnMapReadyCallback = OnMapReadyCallback { map ->
        hidePanel(showPanel)
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        locationManager.getLastLocation { location ->
            val latLng = location.toLatLng()
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 12F)
            )
        }

        map.setOnCameraMoveListener {
            println("Camera Move...")
        }

        map.setOnCameraIdleListener {
            println("Idle ${map.cameraPosition.target}")
        }
    }

    private fun hidePanel(status: Boolean) {
        if (status) {
            showPanel = false
            with(binding) {
                panelLocation.animate()
                    .translationY(panelLocation.measuredHeight.toFloat())
                    .start()
            }
        } else {
            showPanel = true
            with(binding) {
                panelLocation.animate()
                    .translationY(0F)
                    .start()
            }
        }
    }
}