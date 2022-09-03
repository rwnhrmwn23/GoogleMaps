package com.onedev.googlemaps.network.response

data class ReverseLocationResponse(
    val `data`: Data?,
    val message: String?,
    val status: Boolean?
) {
    data class Data(
        val address: Address?,
        val coordinate: Coordinate?,
        val name: String?
    ) {
        data class Address(
            val city: String?,
            val country: String?,
            val district: String?,
            val label: String?,
            val subDistrict: String?
        )

        data class Coordinate(
            val latitude: Double?,
            val longitude: Double?
        )
    }
}