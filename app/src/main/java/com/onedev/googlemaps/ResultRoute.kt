package com.onedev.googlemaps

data class ResultRoutes(
    val `data`: Data?,
    val message: String?,
    val status: Boolean?
) {
    data class Data(
        val route: List<Route?>?
    ) {
        data class Route(
            val latitude: Double?,
            val longitude: Double?
        )
    }
}