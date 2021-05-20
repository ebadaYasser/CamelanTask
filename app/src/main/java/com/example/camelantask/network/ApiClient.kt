package com.example.camelantask.network

import com.example.camelantask.entities.images.ImageResponse
import com.example.camelantask.entities.places.PlacesResponse
import io.reactivex.Single


class ApiClient private constructor() : BaseClientApi() {
    var endPoint: EndPoint = retrofitClient.create(EndPoint::class.java)

    companion object {
        lateinit var apiClient: ApiClient
        fun initApiClient() {
            apiClient = ApiClient()
        }
    }

    fun getPlaces(latLong: String, limit: Int): Single<PlacesResponse> {
        return endPoint.getPlaces(
            RemoteConstants.CLIENT_ID,
            RemoteConstants.CLIENT_SECRET,
            latLong,
            limit,
            1000,
            "20180323"
        )
    }

    fun getImage(id: String): Single<ImageResponse> {
        return endPoint.getPhoto(
            id, RemoteConstants.CLIENT_ID,
            RemoteConstants.CLIENT_SECRET,
            "20180323"
        )
    }
}