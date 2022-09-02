package com.onedev.googlemaps.activity

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.onedev.googlemaps.R
import com.onedev.googlemaps.Sources
import com.onedev.googlemaps.databinding.ActivityMapsBinding
import com.onedev.googlemaps.manager.LocationManager
import com.onedev.googlemaps.utils.moveSmoothly
import com.onedev.googlemaps.utils.toLatLng
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val RC_LOCATION = 23
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var marker: Marker? = null

    private val locationManager: LocationManager by lazy {
        LocationManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.tvResultCoordinate.setOnClickListener {
            locationManager.getLastLocation {
                Toast.makeText(this, it.toLatLng().toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        val routes = Sources.getResultRoutes()
        val coordinates = routes.data?.route.orEmpty()
            .map {
                LatLng(it?.latitude ?: 0.0, it?.longitude ?: 0.0)
            }

        coordinates.firstOrNull()?.let {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15F))
        }

        val polyline = PolylineOptions()
            .addAll(coordinates)
            .width(6F)
            .color(Color.RED)
        mMap.addPolyline(polyline)

        getLocationWithPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(value = RC_LOCATION)
    private fun getLocationWithPermission() {
        val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
        if (EasyPermissions.hasPermissions(this, fineLocation, coarseLocation)) {
            getLocation()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Granted for location",
                RC_LOCATION,
                fineLocation, coarseLocation
            )
        }
    }

    private fun getLocation() {
        MainScope().launch {
            locationManager.getLocationFlow().collect(onLocationResult())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onLocationResult() = FlowCollector<Location> { location ->
        val latLng = LatLng(location.latitude, location.longitude)
        binding.tvResultCoordinate.text = "${latLng.latitude}, ${latLng.longitude}"

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

        if (marker == null) {
            val markerOption = MarkerOptions()
                .position(latLng)
            marker = mMap.addMarker(markerOption)
        }
        marker?.moveSmoothly(latLng)

        println("--------LOCATION UPDATE -> ${location.latitude}, ${location.longitude}")
    }
}