package com.onedev.googlemaps.network

import com.onedev.googlemaps.network.response.ReverseLocationResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    companion object {
        private const val BASE_URL = "https://ojol-api.herokuapp.com/api/"
        fun create(): WebService {
            return  Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebService::class.java)
        }
    }

    object EndPoint {
        const val REVERSE = "v1/location/reverse"
    }

    @GET(EndPoint.REVERSE)
    suspend fun reserveLocation(
        @Query("coordinate") coordinate: String
    ): Response<ReverseLocationResponse>
}