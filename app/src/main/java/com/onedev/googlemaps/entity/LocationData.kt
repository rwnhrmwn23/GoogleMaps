package com.onedev.googlemaps.entity

data class LocationData(
    val address: Address = Address(),
    val coordinate: Coordinate = Coordinate(),
    val name: String = ""
) {
    data class Address(
        val city: String = "",
        val country: String = "",
        val district: String = "",
        val label: String = "",
        val subDistrict: String = ""
    )

    data class Coordinate(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    )
}
