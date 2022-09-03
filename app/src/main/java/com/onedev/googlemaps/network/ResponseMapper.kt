package com.onedev.googlemaps.network

import com.onedev.googlemaps.entity.LocationData
import com.onedev.googlemaps.network.response.ReverseLocationResponse

object ResponseMapper {
    fun mapReverseLocationToLocationData(data: ReverseLocationResponse?): LocationData {
        val address = data?.data?.address.run {
            LocationData.Address(
                city = this?.city.orEmpty(),
                country = this?.country.orEmpty(),
                district = this?.district.orEmpty(),
                label = this?.label.orEmpty(),
                subDistrict = this?.subDistrict.orEmpty(),
            )
        }

        val coordinate = data?.data?.coordinate.run {
            LocationData.Coordinate(
                latitude = this?.latitude ?: 0.0,
                longitude = this?.longitude ?: 0.0
            )
        }

        val name = data?.data?.name.orEmpty()

        return LocationData(
            address = address,
            coordinate = coordinate,
            name = name
        )

    }
}