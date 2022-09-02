package com.onedev.googlemaps.activity

import android.Manifest
import android.os.Bundle
import com.onedev.googlemaps.databinding.ActivityMainBinding
import com.onedev.googlemaps.utils.BaseActivityBinding
import com.onedev.googlemaps.utils.intentTo
import com.onedev.googlemaps.utils.showToast
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivityBinding<ActivityMainBinding>() {
    companion object {
        private const val RC_LOCATION = 23
    }

    override fun inflateBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreateBinding(savedInstanceState: Bundle?) {
        getLocationWithPermission()
    }

    @AfterPermissionGranted(value = RC_LOCATION)
    private fun getLocationWithPermission() {
        val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
        if (EasyPermissions.hasPermissions(this, fineLocation, coarseLocation)) {
            showToast("granted")
            with(binding) {
                btnMaps.setOnClickListener {
                    intentTo(MapsActivity::class.java)
                }

                btnUser.setOnClickListener {
                    intentTo(UserActivity::class.java)
                }

                btnPickerMaps.setOnClickListener {
                    intentTo(MapsPickerActivity::class.java)
                }
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Granted for location",
                RC_LOCATION,
                fineLocation, coarseLocation
            )
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
}