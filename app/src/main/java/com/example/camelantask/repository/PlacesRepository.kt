package com.example.camelantask.repository

import com.example.camelantask.entities.images.ImageResponse
import com.example.camelantask.entities.places.PlacesResponse
import com.example.camelantask.network.ApiClient
import io.reactivex.Single

class PlacesRepository(private val apiClient: ApiClient) {
    fun getPlaces(latLong: String, limit: Int): Single<PlacesResponse> {
        return apiClient.getPlaces(latLong,limit)
    }

    fun getImage(id: String): Single<ImageResponse>{
        return apiClient.getImage(id)
    }
}