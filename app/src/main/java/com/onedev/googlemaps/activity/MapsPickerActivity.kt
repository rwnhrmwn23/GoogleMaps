package com.onedev.googlemaps.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.onedev.googlemaps.R
import com.onedev.googlemaps.databinding.ActivityMapsPickerBinding
import com.onedev.googlemaps.entity.LocationData
import com.onedev.googlemaps.manager.LocationManager
import com.onedev.googlemaps.network.ResponseMapper
import com.onedev.googlemaps.network.WebService
import com.onedev.googlemaps.utils.BaseActivityBinding
import com.onedev.googlemaps.utils.showToast
import com.onedev.googlemaps.utils.toLatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class MapsPickerActivity : BaseActivityBinding<ActivityMapsPickerBinding>() {

    private val locationManager by lazy { LocationManager(this) }

    private val webService by lazy { WebService.create() }

    override fun inflateBinding(): ActivityMapsPickerBinding {
        return ActivityMapsPickerBinding.inflate(layoutInflater)
    }

    override fun onCreateBinding(savedInstanceState: Bundle?) {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReady())
    }

    @SuppressLint("SetTextI18n")
    private fun onMapReady(): OnMapReadyCallback = OnMapReadyCallback { map ->
        hidePanel(true)
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
            hidePanel(true)
        }

        map.setOnCameraIdleListener {
            println("Idle ${map.cameraPosition.target}")
            runBlocking {
                println("onLoading...")
                map.uiSettings.isScrollGesturesEnabled = false
                val coordinate = map.cameraPosition.target
                reverseLocationFlow(coordinate)
                    .debounce(1000L)
                    .collect {
                        println("onResultLocation ${it.name}")
                        with(binding) {
                            tvLocationResult.text = "${it.address.label}\n${it.address.country}"
                        }
                        hidePanel(false)
                        map.uiSettings.isScrollGesturesEnabled = true
                    }
            }
        }
    }

    private fun hidePanel(status: Boolean) {
        with(binding) {
            if (status) {
                panelLocation.animate()
                    .translationY(panelLocation.measuredHeight.toFloat())
                    .start()
            } else {
                panelLocation.animate()
                    .translationY(0F)
                    .start()
            }
        }

    }

    private fun reverseLocationFlow(
        latLng: LatLng
    ): Flow<LocationData> {
        return flow {
            val coordinate = "${latLng.latitude},${latLng.longitude}"
            val response = webService.reserveLocation(coordinate)
            if (response.isSuccessful) {
                val data = ResponseMapper.mapReverseLocationToLocationData(response.body())
                emit(data)
            } else {
                showToast("error : ${response.message()}")
            }
        }
    }
}